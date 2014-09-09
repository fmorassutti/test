package br.com.adp.adpr.test.dao;

/**
 * 
 * @author $Author: fernando $
 * @version $Log: DAOLoadException.java,v $
 * @version Revision 1.1  2012/03/22 20:48:29  fernando
 * @version Refactoring the repository structure.
 * @version
 *
 */
public class DAOLoadException extends Exception {

   private static final long serialVersionUID = -2978487402705189397L;

   public DAOLoadException(String msg) {
      super(msg);
   }
   
   public DAOLoadException(String msg, Throwable cause) {
      super(msg, cause);
   }
}
