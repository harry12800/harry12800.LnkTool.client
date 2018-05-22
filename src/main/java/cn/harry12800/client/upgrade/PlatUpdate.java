package cn.harry12800.client.upgrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import cn.harry12800.j2se.component.NotifyWindow;
import cn.harry12800.j2se.dialog.MessageDialog;
import cn.harry12800.j2se.tip.ActionHandler;
import cn.harry12800.j2se.tip.TipFrame;
import cn.harry12800.j2se.tip.TipFrame.Builder;
import cn.harry12800.tools.FileUtils;
import cn.harry12800.tools.MachineUtils;
import cn.harry12800.tools.XmlUtils;

public class PlatUpdate {
	static Timer timer=new Timer();
	private static class PaltUpdateHanlder{
		static PlatUpdate instance = new PlatUpdate();
	}
	public static PlatUpdate getInstance(){
		return PaltUpdateHanlder.instance;
	}
	private static String updateConfigUrl= Configuration.serverUrl+"/config.dba";
	private PlatUpdate() { }
	public static boolean mark = false;
	/**
	 * 解析加密的服务器更新配置信息。对应xml格式由online.xml一致。
	 * @param xml
	 * @return
	 */
	public static Configuration parse(String xml) {
		Object convertXmlStrToObject = XmlUtils.convertXmlStrToObject(Configuration.class, xml);
//		String convertToXml = XmlUtils.convertToXml(convertXmlStrToObject);
		return (Configuration) convertXmlStrToObject;
//		Configuration configuration = new Configuration();
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder;
//		try {
//			builder = factory.newDocumentBuilder();
//			StringReader sr = new StringReader(xml);
//			InputSource is = new InputSource(sr);
//			Document doc = builder.parse(is);
//			NodeList nodeList = doc.getElementsByTagName("configuration");
//			for (int i = 0; i < nodeList.getLength(); i++) {
//				Node item = nodeList.item(i);
//				NamedNodeMap attributes = item.getAttributes();
//				if( attributes!=null&&attributes.getLength()>0 )
//                {
//                    Node attr=attributes.getNamedItem("version");
//                    String result=attr.getNodeValue();
//                    configuration.version= (result);
//                    attr=attributes.getNamedItem("author");
//                    result=attr.getNodeValue();
//                    configuration.author=result;
//                }
//				NodeList childNodes = item.getChildNodes();
//				for (int j = 0; j< childNodes.getLength(); j++) {
//					Node child = childNodes.item(j);
//					if("description".equals(child.getNodeName())){
//						configuration.description= new ArrayList<String>();
//						NodeList childNodes2 = child.getChildNodes();
//						for (int k = 0; k < childNodes2.getLength(); k++) {
//							Node item1 = childNodes2.item(k);
//							if(!"properties".equals(item1.getNodeName())){
//								continue;
//							}
//							attributes = item1.getAttributes();
//							if( attributes!=null&&attributes.getLength()>0 )
//	                        {
//	                            Node attr=attributes.getNamedItem("value");
//	                            String result=attr.getNodeValue();
//	                            configuration.description.add(result);
//	                        }
//						}
//					}
//					if("resources".equals(child.getNodeName())){
//						configuration.resources= new ArrayList<Resource>();
//						NodeList childNodes2 = child.getChildNodes();
//						for (int k = 0; k < childNodes2.getLength(); k++) {
//							Resource resource = new Resource();
//							Node item1 = childNodes2.item(k);
//							if(!"resource".equals(item1.getNodeName())){
//								continue;
//							}
//							attributes = item1.getAttributes();
//							if( attributes!=null&&attributes.getLength()>0 )
//	                        {
//	                            Node attr=attributes.getNamedItem("name");
//	                            String result=attr.getNodeValue();
//	                            resource.setName(result);
//	                            attr=attributes.getNamedItem("realname");
//	                            result=attr.getNodeValue();
//	                            resource.setRealname(result);
//	                            attr=attributes.getNamedItem("url");
//	                            result=attr.getNodeValue();
//	                            resource.setUrl(result);
//	                            attr=attributes.getNamedItem("path");
//	                            result=attr.getNodeValue();
//	                            resource.setPath(result);
//	                            attr=attributes.getNamedItem("md5");
//	                            result=attr.getNodeValue();
//	                            resource.setMd5(result);
//	                        }
//							configuration.resources.add(resource);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return configuration;

	}

	/**
	 * 下载窗口
	 */
	private static DownLoadFrame downLoadFrame;
	/**
	 * 解析数据
	 */
	private static Configuration parse;
	private static ActionHandler handler = new ActionHandler(){
		@Override
		public void execute() {
			new Thread(){
				public void run() {
					sureUpdate(parse);
				};
			}.start();
		}
	};
	
	/**
	 * 获取downLoadFrame
	 *	@return the downLoadFrame
	 */
	public DownLoadFrame getDownLoadFrame() {
		return downLoadFrame;
	}


	/**
	 * 进入更新
	 * @param parse
	 */
	public synchronized static void sureUpdate(Configuration parse) {
		try {
			mark = true;
			String tmpPath = System.getProperty("user.dir") + File.separator+ "tmp";
			String realPath = System.getProperty("user.dir") + File.separator+ "update";
			System.out.println("aaa+"+ tmpPath );
			List<Resource> resources = parse.resources;
			for (Resource resource : resources) {
				if(!resource.getUrl().startsWith("http")){
					resource.setUrl(Configuration.serverUrl+"/"+resource.getUrl());
				}
			}
			if(downLoadFrame == null) {
//				System.out.println("1");
				downLoadFrame = new DownLoadFrame(parse);
			}
			else {
//				System.out.println("2");
				downLoadFrame.updateResources(parse);
			}
//			System.out.println("画面");
			Thread.sleep(1000);
			int i = 0;
			for (Resource resource : resources) {
				String path = tmpPath + File.separator + resource.getPath()
						+ File.separator + resource.getRealname();
				FileUtils.createDirectory(tmpPath + File.separator
					 	+ resource.getPath());
//				System.out.println("存放路径:" + path);
//				System.out.println("url:" + resource.getUrl());
				downLoadFrame.downloadUrlFile(i,resource.getUrl(), path);
				i++;
				System.out.println("下载完成");
			}
			downLoadFrame.setShowInfo("更新完成");
			downLoadFrame.setVisible(false);
			FileUtils.createDirectory(realPath);
			FileUtils.copyDirectory(tmpPath, realPath);
//			new Thread() {
//				public void run() {
//					JOptionPane.showMessageDialog(null, "更新完成，正在重启!");
//				}
//			}.start();
			FileUtils.deleteDir(tmpPath);
			MachineUtils.reStart();
		} catch (Exception e) {
			if (e instanceof ConnectException) {
				NotifyWindow.error(e.getMessage());
				NotifyWindow.error("远程服务器地址异常！可在config.ini配置中修改,重启。");
			}
			if (e instanceof FileNotFoundException) {
				NotifyWindow.error(e.getMessage());
				NotifyWindow.error("远程服务器地址未发现更新文件信息！与管理员联系");
			}
			if(e instanceof java.net.SocketException){
				NotifyWindow.error("网络异常！");
			}
			downLoadFrame.setShowInfo("更新失败！");
			e.printStackTrace();
		} finally {
			File file = new File(System.getProperty("user.dir")
					+ File.separator + "tmp");
			if (file.exists())
				file.delete();
			mark = false;
		}
	}
	public static void autoUpdate(){
		switch (autoCheckUpdateStatus()) {
		case 0:
			System.out.println("正在更新~！");
			break;
		case 1:
			System.out.println("已经是最新版本吧");
			break;
		case 2:
			System.out.println("可以进入更新");
			Builder builder = TipFrame.createBuilder();
			for (Properties p : parse.description) {
				builder.dataList.add(p.getValue());
			}
			builder.actionName="立即更新";
			builder.handler= handler;
			builder.hasHead=true;
			builder.headTitle="版本更新";
			TipFrame.show(builder );
			//sureUpdate(parse);
			break;
		case 3:
			System.out.println("有异常");
			break;
		default:
			break;
		}
	}
	/**
	 * 监测更新。 1 已经是最新版本，2是需要更新。0，正在更新。
	 * @return
	 */
	private  static int checkUpdateStatus() {
		if(mark)  return 0;
		try {
			String downloadFile = getUrlContent(updateConfigUrl);
			String decode = Encrypt.decode(downloadFile);
			parse = parse(decode);
			System.out.println("本地版本:"+Configuration.selfversion);
			System.out.println("服务器版本:"+parse.version);
			 int verifyVerson = Configuration.verifyVersion(parse.version);
			 System.out.println(verifyVerson);
			if( verifyVerson>0){
				return 2;
			}
			else {
				return 1;
			}
		} catch (Exception e) {
			if(e instanceof ConnectException){
				NotifyWindow.error(e.getMessage());
				NotifyWindow.error("远程服务器地址异常！可在config.ini配置中修改并重启。");
				new MessageDialog(null, "提示", "远程服务器地址异常！可在config.ini配置中修改并重启。");
			}
			else if(e instanceof  FileNotFoundException){
				NotifyWindow.error("远程服务器地址未发现更新文件信息！与管理员联系");
				new MessageDialog(null, "提示", "远程服务器地址未发现更新文件信息！与管理员联系");
			}
			else new MessageDialog(null, "提示", e.getMessage());
			return 3;
		}
	}	
	/**
	 * 监测更新。 1 已经是最新版本，2是需要更新。0，正在更新。
	 * @return
	 */
	private  static int autoCheckUpdateStatus() {
		if(mark)  return 0;
		try {
			String downloadFile = getUrlContent(updateConfigUrl);
			String decode = Encrypt.decode(downloadFile);
			parse = parse(decode);
			System.out.println("本地版本:"+Configuration.selfversion);
			System.out.println("服务器版本:"+parse.version);
			 int verifyVerson = Configuration.verifyVersion(parse.version);
			 System.out.println(verifyVerson);
			if( verifyVerson>0){
				return 2;
			}
			else {
				return 1;
			}
		} catch (FileNotFoundException e) {
			System.out.println("服务器更新文件配置缺失");
			updateConfigUrl=Configuration.spareServerUrl+"/config.dba";
			autoUpdate();
			return 3;
		}catch (Exception e) {
			System.out.println("autoCheckUpdateStatus");
			//e.printStackTrace();
			return 3;
		}
	}	
	public static void main(String[] args) throws Exception {
		String generateMD5 = generateMD5("D:/desktop/harry12800.tools.jar");
		System.out.println(generateMD5);
	}
	static String generateMD5(String path) throws Exception
	{
		String strMD5 = null;
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		MappedByteBuffer buffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		MessageDigest digest = MessageDigest.getInstance("md5");
		digest.update(buffer);
		in.close();
		
		byte[] byteArr = digest.digest();
		BigInteger bigInteger = new BigInteger(1, byteArr);
		strMD5 = bigInteger.toString(16);
		return strMD5;
	}
	
	static String generateMD54ByteArray(String path) throws Exception
	{
		String strMD5 = null;
		MessageDigest digest = MessageDigest.getInstance("md5");
		InputStream in = new FileInputStream(path);
		byte[] buff = new byte[1024];
		int size = -1;
		while((size=in.read(buff))!=-1)
		{
			digest.update(buff, 0, size);
		}
		in.close();
		BigInteger bigInteger = new BigInteger(1, digest.digest());
		strMD5  = bigInteger.toString(16);
		return strMD5;
	}
	/**
	 * 得到url路径的文本文件内容。
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static String getUrlContent(String url) throws Exception {
		String content = null;
		URL source = new URL(url);
		InputStream in = source.openStream();
		content = FileUtils.getStringByStream(in);
		return content;
	}

	public void updateSystem() {
		switch (checkUpdateStatus()) {
		case 0:
			downLoadFrame.setVisible(true);
			break;
		case 1:
			JOptionPane.showMessageDialog(null, "已经是最新版本!");
			break;
		case 2:
			int option = JOptionPane.showConfirmDialog(null, "发现新版本，确定更新吗？","版本提示",JOptionPane.OK_CANCEL_OPTION);
			if(option == 0) {
				sureUpdate(parse);
			}
			break;
		default:
			break;
		}
	}

	public void startPlatUpdate() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				autoUpdate();
			}
		}, 5000,60*60000);
	}
}
