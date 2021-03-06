package facades;

import entities.BannedIPS;
import entities.LoginAttempts;
//import security.Logging;
import utils.MailSystem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MaliciousIntentFacade {

    private static EntityManagerFactory emf;
    private static MaliciousIntentFacade instance;
    /*private static Logging log;

    static {
        try {
            log = Logging.getLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private MaliciousIntentFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static MaliciousIntentFacade getMaliciousIntentFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MaliciousIntentFacade();
        }
        return instance;
    }

    public LoginAttempts logAttempt (LoginAttempts att) {

        EntityManager em = emf.createEntityManager();
        Date date = new Date();
        LoginAttempts attempt = new LoginAttempts(att.getIp(), att.getTargetAccount(), att.getIntent());
        attempt.setTimestamp("" + date.getTime());
        em.getTransaction().begin();
        em.persist(attempt);
        em.getTransaction().commit();

        return attempt;
    }

    public int getLoggedAttempts (LoginAttempts att) {

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        TypedQuery<LoginAttempts> foundIps = em.createQuery("SELECT l FROM LoginAttempts l WHERE l.ip = :ip", LoginAttempts.class);
        foundIps.setParameter("ip", att.getIp());
        em.getTransaction().commit();
        List<LoginAttempts> totalFailedAttempts = foundIps.getResultList();


        int totalCount = 0;
        for (LoginAttempts lA: totalFailedAttempts
             ) {

            Date currentDate = new Date();
            if((Long.parseLong(lA.getTimestamp()) > currentDate.getTime()-36000000)){
                totalCount++;
            }
        }

        em.getTransaction().begin();
        TypedQuery<LoginAttempts> foundIpsWithThisUser = em.createQuery("SELECT l FROM LoginAttempts l WHERE l.ip = :ip AND l.targetAccount = :target", LoginAttempts.class);
        foundIpsWithThisUser.setParameter("ip", att.getIp());
        foundIpsWithThisUser.setParameter("target", att.getTargetAccount());
        em.getTransaction().commit();
        List<LoginAttempts> totalFailedAttemptsForThisAccAndIP = foundIps.getResultList();
        int thisAccCount = 0;
        for (LoginAttempts lA: totalFailedAttemptsForThisAccAndIP
        ) {
            Date currentDate = new Date();
            if(Long.parseLong(lA.getTimestamp()) > currentDate.getTime()-36000000){
                thisAccCount++;
            }
        }
        if(totalCount > 10) {
            return totalCount;
        }
        if(thisAccCount > 3) {
            return thisAccCount;
        }
        em.close();
        return 0;
    }

    public void createBan (LoginAttempts att) {
        UserFacade facade = UserFacade.getUserFacade(emf);
        EntityManager em = emf.createEntityManager();
        MailSystem ms = new MailSystem();
        Date date = new Date();
        String banTime = "";
        banTime += date.getTime()+36000000;

        BannedIPS ban = new BannedIPS(att.getIp(), banTime);
        String user = "";
        if(facade.findUserByUsername(att.getTargetAccount()) != null) {
            user = att.getTargetAccount();
            facade.setUserRecoveryKey(user, att.getIp());
            ms.sendWarningForUser(user, att.getIp());
        }
        em.getTransaction().begin();
        BannedIPS banFound = em.find(BannedIPS.class, att.getIp());
        if(banFound != null) {
            ban = banFound;
            user += ban.getBannedFromAccount();
            ban.setBannedFromAccount(user);
            em.merge(ban);
        } else {
            ban.setBannedFromAccount(user);
            em.persist(ban);
        }
        em.getTransaction().commit();
        em.close();
       /* try {
            log.warningLog("Ban was created for ip: " + att.getIp());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void liftBan (String mail, String ip) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<BannedIPS> deleteMatch = em.createQuery("DELETE FROM BannedIPS b WHERE b.iP = :ip AND b.bannedFromAccount = :mail", BannedIPS.class);
        deleteMatch.setParameter("ip", ip);
        deleteMatch.setParameter("mail", mail);
        TypedQuery<LoginAttempts> deleteMatchingLoginAttempts = em.createQuery("DELETE FROM LoginAttempts l WHERE l.ip = :ip AND l.targetAccount = :mail", LoginAttempts.class);
        deleteMatchingLoginAttempts.setParameter("ip", ip);
        deleteMatchingLoginAttempts.setParameter("mail", mail);
        deleteMatch.executeUpdate();
        deleteMatchingLoginAttempts.executeUpdate();
        em.getTransaction().commit();
        em.close();
        /*try {
            log.infoLog(ip + " has lifted their ban");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public boolean isBanned (LoginAttempts att) {

        EntityManager em = emf.createEntityManager();
        if(em.find(BannedIPS.class, att.getIp()) == null) {
            return false;
        }
        BannedIPS banFound = em.find(BannedIPS.class, att.getIp());
        long currentDate = new Date().getTime();

        if(Long.parseLong(banFound.getBannedUntil()) >= currentDate &&
                (banFound.getBannedFromAccount() != null || banFound.getBannedFromAccount() == "")) {
            return true;
        } else if (Long.parseLong(banFound.getBannedUntil()) >= currentDate) {
            return banFound.getBannedFromAccount() != null && banFound.getBannedFromAccount().contains(att.getTargetAccount());
        }
        /*try {
            log.warningLog(att.getIp() + " which is banned, is trying to access the site again");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return false;
    }
}
