/**
 * 
 */
package soars.library.adapter.pubsub;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soars.library.adapter.userrules.Unit;
import soars.library.adapter.userrules.UserRuleUtility;
import ssac.aadl.runtime.mqtt.MqttBufferedSession;
import ssac.aadl.runtime.mqtt.MqttConnectionParams;
import ssac.aadl.runtime.mqtt.MqttPublisher;
import ssac.aadl.runtime.mqtt.MqttSubscriber;
import ssac.aadl.runtime.mqtt.MqttUtil;
import util.DoubleValue;
import util.IntValue;
import env.EquippedObject;

/**
 * @author kurata
 *
 */
public class PubSub {

	/**
	 * 
	 */
	static Map<EquippedObject, MqttBufferedSession> _mqttBufferedSessionMap = new HashMap<EquippedObject, MqttBufferedSession>();

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 */
	public static void initialize(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
		String serverURI = UserRuleUtility.getString( equippedObject, arg1);
		Object port = Unit.get( equippedObject, arg2);
		String clientID = UserRuleUtility.getString( equippedObject, arg3);
		String user = UserRuleUtility.getString( equippedObject, arg4);
		String password = UserRuleUtility.getString( equippedObject, arg5);
		if ( null == serverURI)
			_mqttBufferedSessionMap.put( equippedObject, MqttUtil.connectBufferedSession());
		else {
			MqttConnectionParams mqttConnectionParams = new MqttConnectionParams();

			if ( null == port)
				mqttConnectionParams.setServerURI( serverURI);
			else
				mqttConnectionParams.setServerURI( serverURI, UserRuleUtility.getIntValue( equippedObject, arg2));

			if ( null != clientID)
				mqttConnectionParams.setClientID( clientID);

			if ( null != user)
				mqttConnectionParams.getOptions().setUserName( user);

			if ( null != password)
				mqttConnectionParams.getOptions().setPassword( password);

			_mqttBufferedSessionMap.put( equippedObject, MqttUtil.connectBufferedSession( mqttConnectionParams));
		}
//		JOptionPane.showMessageDialog( null,
//			equippedObject.getEquip( "$Name") + CommonConstant._lineSeparator
//				+ UserRuleUtility.getString( equippedObject, arg1) + CommonConstant._lineSeparator
//				+ UserRuleUtility.getString( equippedObject, arg2) + CommonConstant._lineSeparator,
//			"initialize",
//			JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public static void publish(EquippedObject equippedObject, String arg0, String arg1, String arg2) {
		String topic = UserRuleUtility.getString( equippedObject, arg1);
		Object object = UserRuleUtility.get( equippedObject, arg2);
		if ( object instanceof String)
			MqttPublisher.publishString( _mqttBufferedSessionMap.get( equippedObject), topic, ( String)object);
		else if ( ( object instanceof IntValue) || ( object instanceof DoubleValue))
			MqttPublisher.publishString( _mqttBufferedSessionMap.get( equippedObject), topic, String.valueOf( object));
	}

	/**
	 * @param message
	 */
	private static void debug(String message) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( new File( "data.txt")));
			outputStreamWriter.write( message);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void subscribe(EquippedObject equippedObject, String arg0, String arg1) {
		List<String> list = ( List<String>)UserRuleUtility.get( equippedObject, arg1);
		MqttSubscriber.subscribe( _mqttBufferedSessionMap.get( equippedObject), list);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void waitAllMessages(EquippedObject equippedObject, String arg0, String arg1) {
		List<String> list = ( List<String>)UserRuleUtility.get( equippedObject, arg1);
		try {
			MqttSubscriber.waitAllMessages( _mqttBufferedSessionMap.get( equippedObject), list);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void unsubscribe(EquippedObject equippedObject, String arg0, String arg1) {
		List<String> list = ( List<String>)UserRuleUtility.get( equippedObject, arg1);
		MqttSubscriber.unsubscribe( _mqttBufferedSessionMap.get( equippedObject), list);
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 */
	public static void disconnect(EquippedObject equippedObject, String arg0) {
		MqttUtil.disconnect( _mqttBufferedSessionMap.get( equippedObject));
	}

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public static void popFilteredString(EquippedObject equippedObject, String arg0, String arg1, String arg2, String arg3) {
		EquippedObject eo = Unit.getEntity( equippedObject, arg1);
		String topic = UserRuleUtility.getString( equippedObject, arg2);
		try {
			String message = MqttSubscriber.popFilteredString( _mqttBufferedSessionMap.get( eo), topic);
			UserRuleUtility.set( equippedObject, arg3, message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
