/*
 * Created on 02/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package es.caib.zkiblaf.applet;

import java.applet.Applet;
import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.swing.JOptionPane;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;



import es.caib.signatura.api.Signature;
import es.caib.signatura.api.SignatureCertNotFoundException;
import es.caib.signatura.api.SignaturePrivKeyException;
import es.caib.signatura.api.SignatureSignException;
import es.caib.signatura.api.SignerFactory;
import es.caib.signatura.api.UpgradeNeededException;
import es.caib.signatura.impl.CAIBSigner;

/**
 * @author u07286
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SignApplet extends Applet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String signPDF_url;
    private String signPDF_position;
    private String certifyPDF_x;
    private String certifyPDF_y;
    private String certifyPDF_degrees;
    private String certifyPDF_url;
	private String certifyPDF_location;
	private String signPDFExtended_top;
	private String signPDFExtended_left;
	private String signPDFExtended_height;
	private String signPDFExtended_width;
	private String signPDFExtended_rotation;
	private String contentType;
	private es.caib.signatura.api.Signer signer = null;
	private URLConnection sourceConnection;

    
	/**
	 * @throws java.awt.HeadlessException
	 */
	public SignApplet() throws HeadlessException {
	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#start()
	 */
	public void start() {
		super.start();
	}
	
	public String sign (String cert, String source)
	{
		
		if (! parseContentType(source))
			return "ERROR";
		
		initSignature();
		
		
		Signature signatureData;
		try{
			PinCache cache = PinCache.getInstance();
			char pass[] = cache.getPassword(cert);
			if (pass == null)
				return "ERROR";
			
			signatureData = signer.sign(sourceConnection.getInputStream(),
					cert,
					new String(pass), 
					contentType);
		}
		catch (IOException io)
		{
			showMessageError("Error d'entrada-sortida: "+io.toString());
			return "ERROR";
		}
		catch (SignatureSignException e1)
		{
			showMessageError("No s'ha pogut signar: "+e1.toString());
			return "ERROR";
		}
		catch (Throwable se)
		{
			se.printStackTrace();
			Throwable root = se;
			while (root.getCause() != null)
				root = root.getCause();
			String message = "Error no previst: ";
			if (root.getMessage() != null)
				message = message+" "+root.getMessage();
			else
				message = message+" ["+root.getClass().getName()+"]";
			showMessageError(message);
			return "ERROR";
		} finally {
		}
		
		
		try {
			// Generar la firma
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(o);
			oout.writeObject(signatureData);
			oout.close();
			o.close();
			byte b[]=o.toByteArray();
			byte coded[] = Base64.toBase64(b,0,b.length);
			String s  = new String (coded, "ISO-8859-1");
//			showMessageError("zkau.send({uuid: \""+getParameter("zkuuid")+"\", cmd: \"onSign\", data: ["+s+"]});");
			JSObject win = JSObject.getWindow(this);
			JSObject document = (JSObject) win.getMember("document");
			document.eval("zkau.send({uuid: \""+getParameter("zkuuid")+"\", cmd: \"onSign\", data: [\""+s+"\"]});");
			return "OK";
		}
		catch (IOException io)
		{
			io.printStackTrace();
			showMessageError("Error d'entrada sortida: "+io.getMessage());
			return "ERROR";
		}
		
	}

        
    public String signPDF (String cert, String source, String target)
    {
            
            if (! parseContentType(source))
                    return "ERROR";
            
            initSignature();//contentType
            
            
            try{
            		//Obtenim el pin
                    PinCache cache = PinCache.getInstance();
                    char pass[] = cache.getPassword(cert);
                    if (pass == null)
                            return "ERROR";
                    
                    //preparem l'objecte destí
                    URL targetURL;
                    try {
                    	targetURL = new URL (target);
                    } catch (MalformedURLException e) {
                            showMessageError("URL incorrecta "+target);
                            throw e;
                    }
                    
                    HttpURLConnection connection;
                    try {
                            connection = (HttpURLConnection) targetURL.openConnection();
                    } catch (IOException e1) {
                            showMessageError("URL inaccesible "+target);
                            return null;
                    }
                    
                    try {
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("PUT");
                        connection.setRequestProperty("Content-Type","application/pdf");
                            connection.connect();
 
                        } catch (Exception e2) {
                            showMessageError (e2.toString());
                            e2.printStackTrace();
                            return "ERROR";
                    }

                   
                    OutputStream out=connection.getOutputStream();
                    
                    if (out == null)
                        return "ERROR";
                    
                    //iniciem la firma al destí 
                    signer.signPDF(sourceConnection.getInputStream(),
                    		out,
                            cert,
                            new String(pass),
                            contentType, 
                            signPDF_url, Integer.parseInt(signPDF_position),true);
                    
                   	out.close();
                   	//new String(Base64.encodeBase64(((ByteArrayOutputStream)out).toByteArray())).toString();
                   	
                   	//recuperem la sortida del servlet destí
        	        InputStream in=connection.getInputStream();
        	        StringBuffer buff=new StringBuffer();
        	        
        	        int val;
        	        while(-1 != (val=in.read())){
        	        	buff.append((char)val);
        	        }
        	        
        	        //System.out.println(buff.toString());
            }
            catch (IOException io)
            {
                    showMessageError("Error d'entrada-sortida: "+io.toString());
                    return "ERROR";
            }
            catch (SignatureSignException e1)
            {
                    showMessageError("No s'ha pogut signar: "+e1.toString());
                    return "ERROR";
            }
            catch (Throwable se)
            {
                    se.printStackTrace();
                    Throwable root = se;
                    while (root.getCause() != null)
                            root = root.getCause();
                    String message = "Error no previst: ";
                    if (root.getMessage() != null)
                            message = message+" "+root.getMessage();
                    else
                            message = message+" ["+root.getClass().getName()+"]";
                    showMessageError(message);
                    return "ERROR";
            } finally {
            }
            
            
            try {
                    // Generar la firma
                    JSObject win = JSObject.getWindow(this);
                    JSObject document = (JSObject) win.getMember("document");
                    document.eval("zkau.send({uuid: \""+getParameter("zkuuid")+"\", cmd: \"onSign\", data: [\"\"]});");
                    return "OK";
            }
            catch (Exception io)
            {
                    io.printStackTrace();
                    showMessageError("Error d'entrada sortida: "+io.getMessage());
                    return "ERROR";
            }
            
    }
    
 
    public String signPDFExtended (String cert, String source, String target)
    {
            
            if (! parseContentType(source))
                    return "ERROR";
            
            initSignature();//contentType
            
            
            try{
            		//Obtenim el pin
                    PinCache cache = PinCache.getInstance();
                    char pass[] = cache.getPassword(cert);
                    if (pass == null)
                            return "ERROR";
                    
                    //preparem l'objecte destí
                    URL targetURL;
                    try {
                    	targetURL = new URL (target);
                    } catch (MalformedURLException e) {
                            showMessageError("URL incorrecta "+target);
                            throw e;
                    }
                    
                    HttpURLConnection connection;
                    try {
                            connection = (HttpURLConnection) targetURL.openConnection();
                    } catch (IOException e1) {
                            showMessageError("URL inaccesible "+target);
                            return null;
                    }
                    
                    try {
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("PUT");
                        connection.setRequestProperty("Content-Type","application/pdf");
                            connection.connect();
 
                        } catch (Exception e2) {
                            showMessageError (e2.toString());
                            e2.printStackTrace();
                            return "ERROR";
                    }

                   
                    OutputStream out=connection.getOutputStream();
                    
                    if (out == null)
                        return "ERROR";
                    
                    //iniciem la firma al destí 
                    //invoquem així el mètode perquè el jar de la api pot no contindre declarat aquest mètode a la interfície Signer
                    //així que recuperem dinàmicament el mètode de la implementació del signer que ens vé
                    Method method=null;
                    try{
                    	((CAIBSigner)signer).signPDF(
                    			sourceConnection.getInputStream()
	                    		,out
	                            ,cert
	                            ,new String(pass)
	                            ,contentType 
	                            ,signPDF_url
	                            ,Integer.parseInt(signPDF_position)
	                            ,Float.parseFloat(signPDFExtended_top)
	                            ,Float.parseFloat(signPDFExtended_left)
	                            ,Float.parseFloat(signPDFExtended_height)
	                            ,Float.parseFloat(signPDFExtended_width)
	                            ,Float.parseFloat(signPDFExtended_rotation)
	                            ,true
	                    	);

                    }finally{
                        out.close();
                   	
                    }

                   	//new String(Base64.encodeBase64(((ByteArrayOutputStream)out).toByteArray())).toString();

                    //si hi ha hagut error al servidor
        	        if(connection.getResponseCode()!=200){
                        showMessageError("No s'ha pogut enviar la firma: Error amb la sessió. Cal reiniciar el navegador d'internet.");
                        return "ERROR";
        	        }
                   	
                   	//recuperem la sortida del servlet destí
                    InputStream in=connection.getInputStream();
        	        StringBuffer buff=new StringBuffer();
        	        
        	        int val;
        	        while(-1 != (val=in.read())){
        	        	buff.append((char)val);
        	        }
        	        
        	        //System.out.println(buff.toString());
            }
            catch (IOException io)
            {
                    showMessageError("Error d'entrada-sortida: "+io.toString());
                    return "ERROR";


            }catch (Throwable se)
            {
                    se.printStackTrace();
                    Throwable root = se;
                    while (root.getCause() != null)
                            root = root.getCause();
                    String message = "Error no previst: ";
                    if (root.getMessage() != null)
                            message = message+" "+root.getMessage();
                    else
                            message = message+" ["+root.getClass().getName()+"]";
                    showMessageError(message);
                    return "ERROR";
            } finally {
            }
            
            
            try {
                    // Generar la firma
                    JSObject win = JSObject.getWindow(this);
                    JSObject document = (JSObject) win.getMember("document");
                    document.eval("zkau.send({uuid: \""+getParameter("zkuuid")+"\", cmd: \"onSign\", data: [\"\"]});");
                    return "OK";
            }
            catch (Exception io)
            {
                    io.printStackTrace();
                    showMessageError("Error d'entrada sortida: "+io.getMessage());
                    return "ERROR";
            }
            
    }
    
    public String certifyPDF (String cert, String source, String target)
    {
            
            if (! parseContentType(source))
                    return "ERROR";
            
            initSignature();//contentType
            
            
            try{
            		//Obtenim el pin
                    PinCache cache = PinCache.getInstance();
                    char pass[] = cache.getPassword(cert);
                    if (pass == null)
                            return "ERROR";
                    
                    //preparem l'objecte destí
                    URL targetURL;
                    try {
                    	targetURL = new URL (target);
                    } catch (MalformedURLException e) {
                            showMessageError("URL incorrecta "+target);
                            throw e;
                    }
                    
                    HttpURLConnection connection;
                    try {
                            connection = (HttpURLConnection) targetURL.openConnection();
                    } catch (IOException e1) {
                            showMessageError("URL inaccesible "+target);
                            return null;
                    }
                    
                    try {
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("PUT");
                        connection.setRequestProperty("Content-Type","application/pdf");
                            connection.connect();
 
                        } catch (Exception e2) {
                            showMessageError (e2.toString());
                            e2.printStackTrace();
                            return "ERROR";
                    }

                   
                    OutputStream out=connection.getOutputStream();
                    
                    if (out == null)
                        return "ERROR";
                    
                    //iniciem la firma al destí 
                    signer.certifyDigitalCopy(sourceConnection.getInputStream(),
                    		out,
                            cert,
                            new String(pass),
                            contentType, 
                            certifyPDF_url,
                            certifyPDF_location,
                            Float.parseFloat(certifyPDF_x),
                            Float.parseFloat(certifyPDF_y),
                            Float.parseFloat(certifyPDF_degrees));
                    
                   	out.close();
                   	//new String(Base64.encodeBase64(((ByteArrayOutputStream)out).toByteArray())).toString();
                   	
                   	//recuperem la sortida del servlet destí
        	        InputStream in=connection.getInputStream();
        	        StringBuffer buff=new StringBuffer();
        	        
        	        int val;
        	        while(-1 != (val=in.read())){
        	        	buff.append((char)val);
        	        }
        	        
        	        //System.out.println(buff.toString());
            }
            catch (IOException io)
            {
                    showMessageError("Error d'entrada-sortida: "+io.toString());
                    return "ERROR";
            }
            catch (SignatureSignException e1)
            {
                    showMessageError("No s'ha pogut signar: "+e1.toString());
                    return "ERROR";
            }
            catch (Throwable se)
            {
                    se.printStackTrace();
                    Throwable root = se;
                    while (root.getCause() != null)
                            root = root.getCause();
                    String message = "Error no previst: ";
                    if (root.getMessage() != null)
                            message = message+" "+root.getMessage();
                    else
                            message = message+" ["+root.getClass().getName()+"]";
                    showMessageError(message);
                    return "ERROR";
            } finally {
            }
            
            
            try {
                    // Generar la firma
                    JSObject win = JSObject.getWindow(this);
                    JSObject document = (JSObject) win.getMember("document");
                    document.eval("zkau.send({uuid: \""+getParameter("zkuuid")+"\", cmd: \"onSign\", data: [\"\"]});");
                    return "OK";
            }
            catch (Exception io)
            {
                    io.printStackTrace();
                    showMessageError("Error d'entrada sortida: "+io.getMessage());
                    return "ERROR";
            }
            
    }
    
    
    private OutputStream createOutputStream(String url) {
        URL sourceURL;
        try {
                sourceURL = new URL (url);
        } catch (MalformedURLException e) {
                showMessageError("URL incorrecta "+url);
                return null;
        }
        HttpURLConnection connection;
        try {
                connection = (HttpURLConnection) sourceURL.openConnection();
        } catch (IOException e1) {
                showMessageError("URL inaccesible "+url);
                return null;
        }
        
        try {
            connection.setDoInput(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.connect();
            return connection.getOutputStream();
        } catch (Exception e2) {
            showMessageError (e2.toString());
            e2.printStackTrace();
            return null;
        }
    }


    private boolean parseContentType(String source) {
		URL sourceURL;
		try {
			sourceURL = new URL (getDocumentBase(), source);
		} catch (MalformedURLException e) {
			showMessageError("URL incorrecta "+source);
			return false;
		}
		try {
			sourceConnection = sourceURL.openConnection();
		} catch (IOException e1) {
			showMessageError("URL inaccesible "+source);
			return false;
		}
		
		try {
			((HttpURLConnection)sourceConnection).setRequestMethod("GET");
			sourceConnection.connect();
			contentType = sourceConnection.getContentType();
		} catch (Exception e2) {
			showMessageError (e2.toString());
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	

	
	public void init ()
	{
	}
	
	private void initSignature()
	{
		//recuperamos la configuración del sistema de firma
		String signaturaPropertiesServletURL=getParameter("signaturaPropertiesServletURL");
		SignerFactory sf=null;
		try {	
			if(signaturaPropertiesServletURL!=null){			
				Properties p=new Properties();
				p.load(new URL(signaturaPropertiesServletURL).openStream());
				sf = new SignerFactory(p);

			}else{
				showMessageError("Error en la configuración de la aplicación: configuración de las propiedades de firma");
			}
		

			signer = sf.getSigner();		
		} catch (UpgradeNeededException e) {
			e.printStackTrace();
			showMessageError("Es necessari actualitzar el sistema de firma");
			return;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			showMessageError("Error en la configuración de la firma: "+e.getMessage());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			showMessageError("Error en la configuración de la firma: "+e.getMessage());
			return;
		}
		
	}
	
	public String [] getCerts (String source) throws IOException {
		initSignature ();
		
		parseContentType(source);
		if (! parseContentType(source))
			return new String [0];
		sourceConnection.getInputStream().close();
		sourceConnection =  null;
		
		try {
			return signer.getCertList(contentType);
		}
		catch (SignaturePrivKeyException ex)
		{
			showMessageError(ex.toString());
			return new String[0];
		}
		catch (SignatureCertNotFoundException cex)
		{
			showMessageError(cex.toString());
			return new String[0];
		}
		
	}
	
	public void loadCerts (String source) throws IOException {
		try { 
			String certs [] = getCerts (source);
			StringBuffer certString = new StringBuffer ("[");
			for (int i= 0; i < certs.length; i++)
			{
				if ( i > 0  )
					certString.append(", ");
				certString.append('"');
				certString.append(certs[i].replace('\"', '\''));
				certString.append('"');
			}
			certString.append( " ]");
			JSObject win = JSObject.getWindow(this);
			JSObject document = (JSObject) win.getMember("document");
			document.eval("zkau.send({uuid: \""+getParameter("zkuuid")+"\", cmd: \"onLoadCerts\", data: "+certString+"});");
		}
		catch (JSException e)
		{
			e.printStackTrace();
			showMessageError("Error js:"+e.getMessage()+"("+e.getWrappedExceptionType()+"="+((e.getWrappedException()==null)?e.getWrappedException():e.getWrappedException().toString())+")");
		}
		catch (Exception e) 
		{
			showMessageError(e.toString());
		}
		
	}
	
	
	
	
	
	private void showMessageError(String message)
	{
		JOptionPane.showMessageDialog(null, message, "Govern Balear: Signatura Digital", JOptionPane.ERROR_MESSAGE);
	}

	
	/**
	 * @param signPDFUrl the signPDF_url to set
	 */
	public synchronized void setSignPDF_url(String signPDFUrl) {
		signPDF_url = signPDFUrl;
	}


	/**
	 * @param signPDFPosition the signPDF_position to set
	 */
	public synchronized void setSignPDF_position(String signPDFPosition) {
		signPDF_position = signPDFPosition;
	}



	/**
	 * @param certifyPDFX the certifyPDF_x to set
	 */
	public synchronized void setCertifyPDF_x(String certifyPDFX) {
		certifyPDF_x = certifyPDFX;
	}



	/**
	 * @param certifyPDFY the certifyPDF_y to set
	 */
	public synchronized void setCertifyPDF_y(String certifyPDFY) {
		certifyPDF_y = certifyPDFY;
	}



	/**
	 * @param certifyPDFDegrees the certifyPDF_degrees to set
	 */
	public synchronized void setCertifyPDF_degrees(String certifyPDFDegrees) {
		certifyPDF_degrees = certifyPDFDegrees;
	}

	/**
	 * @param certifyPDFUrl the certifyPDF_url to set
	 */
	public synchronized void setCertifyPDF_url(String certifyPDFUrl) {
		certifyPDF_url = certifyPDFUrl;
	}
	
	/**
	 * @param certifyPDFUrl the certifyPDF_url to set
	 */
	public synchronized void setCertifyPDF_location(String certifyPDFLocation) {
		certifyPDF_location = certifyPDFLocation;
	}

	/**
	 * @param signPDFExtendedTop the signPDFExtended_top to set
	 */
	public synchronized void setSignPDFExtended_top(String signPDFExtendedTop) {
		signPDFExtended_top = signPDFExtendedTop;
	}

	/**
	 * @param signPDFExtendedLeft the signPDFExtended_left to set
	 */
	public synchronized void setSignPDFExtended_left(String signPDFExtendedLeft) {
		signPDFExtended_left = signPDFExtendedLeft;
	}

	/**
	 * @param signPDFExtendedHeight the signPDFExtended_height to set
	 */
	public synchronized void setSignPDFExtended_height(String signPDFExtendedHeight) {
		signPDFExtended_height = signPDFExtendedHeight;
	}

	/**
	 * @param signPDFExtendedWidth the signPDFExtended_width to set
	 */
	public synchronized void setSignPDFExtended_width(String signPDFExtendedWidth) {
		signPDFExtended_width = signPDFExtendedWidth;
	}

	/**
	 * @param signPDFExtendedRotation the signPDFExtended_rotation to set
	 */
	public synchronized void setSignPDFExtended_rotation(String signPDFExtendedRotation) {
		signPDFExtended_rotation = signPDFExtendedRotation;
	}
	
	
	

	


}
