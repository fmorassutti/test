package br.com.adp.adpr.test.dao;

/**
 * 
 * @author $Author: fernando $
 * @version $Log: DAOLoadListException.java,v $
 * @version Revision 1.1  2012/03/22 20:48:29  fernando
 * @version Refactoring the repository structure.
 * @version
 *
 */
public class DAOLoadListException extends Exception {

   private static final long serialVersionUID = 6799372419835723500L;

   public DAOLoadListException(String msg) {
      super(msg);
   }

   public DAOLoadListException(String msg, Throwable cause) {
      super(msg, cause);
   }

}
