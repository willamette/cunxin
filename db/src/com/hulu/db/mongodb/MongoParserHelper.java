package com.hulu.db.mongodb;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class MongoParserHelper extends DefaultHandler {
	static class AdapterParam {
		AdapterParam(String name) {
			this.name = name;
		}
		String name;
		List<ServerAddress> serverList = new LinkedList<ServerAddress>();

		public MongoDBAdapter generateMongoAdapter() {
			MongoOptions options = new MongoOptions();
			options.connectionsPerHost = 100;
			return new MongoDBAdapter(serverList, options);
		}
	}

	private HashMap<String, MongoDBAdapter> _container;
	private AdapterParam _adapter;
	private Locator _locator;

	public MongoParserHelper(HashMap<String, MongoDBAdapter> container) {
		_container = container;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXParseException {
		if ("tns:connection".equals(qName)) {
			_adapter = new AdapterParam(attributes.getValue("name"));
		} else if ("tns:server".equals(qName)) {
			String portString = attributes.getValue("port");
			int port;
			try {
				port = portString==null?ServerAddress.defaultPort():Integer.parseInt(portString);
			} catch (NumberFormatException e) {
				port = ServerAddress.defaultPort();
			}
			try {
				_adapter.serverList.add(new ServerAddress(attributes.getValue("host"), port));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else if ("tns:connections".equals(qName)) {
			//DO nothing
		} else {
			throw new SAXParseException("Unexpected path", _locator);
		}
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		this._locator = locator;
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if ("tns:connection".equals(qName)) {
			_container.put(_adapter.name, _adapter.generateMongoAdapter());
		}
	}

	public Map<String, MongoDBAdapter> getResult() {
		return _container;
	}
}
