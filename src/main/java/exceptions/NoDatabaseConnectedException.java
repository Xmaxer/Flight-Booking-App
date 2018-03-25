package exceptions;

public class NoDatabaseConnectedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1629101380891040760L;

	public NoDatabaseConnectedException(String msg)
	{
		super(msg);
	}
	
}
