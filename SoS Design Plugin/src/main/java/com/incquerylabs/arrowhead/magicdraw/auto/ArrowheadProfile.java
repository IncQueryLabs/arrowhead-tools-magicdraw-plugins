package com.incquerylabs.arrowhead.magicdraw.auto;

import com.google.gson.annotations.SerializedName;
import com.nomagic.uml2.ext.jmi.helpers.StereotypeByProfileCache;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.uml.BaseElement;

import java.util.Collection;
import java.util.HashSet;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@SuppressWarnings("all")
public class ArrowheadProfile extends StereotypeByProfileCache {
	
	public static final String PROFILE_URI = "";
	
	public static final String PROFILE_NAME = "Arrowhead";
	
	public static ArrowheadProfile getInstance(@Nonnull BaseElement element) {
		return getInstance(Project.getProject(element));
	}
	
	public ArrowheadProfile(@Nonnull Project project) {
		super(project, PROFILE_NAME, PROFILE_URI);
	}
	
	public static ArrowheadProfile getInstance(@Nonnull Project project) {
		ArrowheadProfile instance = getInternalInstance(ArrowheadProfile.class, project);
		if(instance == null) {
			instance = new ArrowheadProfile(project);
		}
		return instance;
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_DATATYPE = "HttpMethodKind";
	@SuppressWarnings("UnusedDeclaration")
	public static final String SECURITYKIND_DATATYPE = "SecurityKind";
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Enumeration getHttpMethodKind() {
		return (Enumeration) getDataType(HTTPMETHODKIND_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Enumeration getSecurityKind() {
		return (Enumeration) getDataType(SECURITYKIND_DATATYPE);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_CONNECT_LITERAL = "CONNECT";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_DELETE_LITERAL = "DELETE";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_GET_LITERAL = "GET";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_HEAD_LITERAL = "HEAD";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_OPTIONS_LITERAL = "OPTIONS";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_PATCH_LITERAL = "PATCH";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_POST_LITERAL = "POST";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_PUT_LITERAL = "PUT";
	@SuppressWarnings("UnusedDeclaration")
	public static final String HTTPMETHODKIND_TRACE_LITERAL = "TRACE";
	
	public enum HttpMethodKindEnum {
		GET(HTTPMETHODKIND_GET_LITERAL), PUT(HTTPMETHODKIND_PUT_LITERAL), POST(HTTPMETHODKIND_POST_LITERAL), DELETE(
			HTTPMETHODKIND_DELETE_LITERAL), PATCH(HTTPMETHODKIND_PATCH_LITERAL), HEAD(
			HTTPMETHODKIND_HEAD_LITERAL), CONNECT(HTTPMETHODKIND_CONNECT_LITERAL), OPTIONS(
			HTTPMETHODKIND_OPTIONS_LITERAL), TRACE(HTTPMETHODKIND_TRACE_LITERAL);
		private String text;
		
		HttpMethodKindEnum(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		@CheckForNull
		public static HttpMethodKindEnum from(@CheckForNull Object o) {
			String text = null;
			if(o instanceof EnumerationLiteral) {
				text = ((EnumerationLiteral) o).getName();
			} else if(o != null) {
				text = o.toString();
			}
			if(text != null) {
				for(HttpMethodKindEnum b : HttpMethodKindEnum.values()) {
					if(text.equals(b.text)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static final String SECURITYKIND_CERTIFICATE_LITERAL = "CERTIFICATE";
	@SuppressWarnings("UnusedDeclaration")
	public static final String SECURITYKIND_NOT_SECURE_LITERAL = "NOT_SECURE";
	@SuppressWarnings("UnusedDeclaration")
	public static final String SECURITYKIND_TOKEN_LITERAL = "TOKEN";
	
	public enum SecurityKindEnum {
		
		@SerializedName(SECURITYKIND_NOT_SECURE_LITERAL) NOT_SECURE(SECURITYKIND_NOT_SECURE_LITERAL),
		
		@SerializedName(SECURITYKIND_CERTIFICATE_LITERAL) CERTIFICATE(SECURITYKIND_CERTIFICATE_LITERAL),
		
		@SerializedName(SECURITYKIND_TOKEN_LITERAL) TOKEN(SECURITYKIND_TOKEN_LITERAL);
		
		private String text;
		
		SecurityKindEnum(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		@CheckForNull
		public static SecurityKindEnum from(@CheckForNull Object o) {
			String text = null;
			if(o instanceof EnumerationLiteral) {
				text = ((EnumerationLiteral) o).getName();
			} else if(o != null) {
				text = o.toString();
			}
			if(text != null) {
				for(SecurityKindEnum b : SecurityKindEnum.values()) {
					if(text.equals(b.text)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	
	public static class DataSemantics extends AbstractStereotypeWrapper {
		
		//stereotype DataSemantics and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "DataSemantics";
		@SuppressWarnings("UnusedDeclaration")
		public static final String ONTOLOGY = "Ontology";
		@SuppressWarnings("UnusedDeclaration")
		public static final String SCHEMA = "Schema";
		@SuppressWarnings("UnusedDeclaration")
		public static final String SEMANTICMODEL = "SemanticModel";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setOntology(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDataSemantics(), ONTOLOGY, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearOntology(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getDataSemantics(), ONTOLOGY, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getOntology(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDataSemantics(), ONTOLOGY));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setSchema(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDataSemantics(), SCHEMA, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearSchema(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getDataSemantics(), SCHEMA, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getSchema(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getDataSemantics(), SCHEMA));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setSemanticModel(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDataSemantics(), SEMANTICMODEL, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearSemanticModel(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDataSemantics(), SEMANTICMODEL, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getSemanticModel(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDataSemantics(), SEMANTICMODEL));
		}
		
	}
	
	/**
	 * The DataSemantics stereotype shall be used for IDD parameters: its purpose is to declare the data model (i.e., a
	 * language, an ontology, a schema or similar) used for structuring parameter values.
	 *
	 * @return stereotype
	 */
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getDataSemantics() {
		return getStereotype(DataSemantics.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isDataSemantics(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getDataSemantics());
		}
		return false;
	}
	
	public static class DeployedDevice extends AbstractStereotypeWrapper {
		
		//stereotype DeployedDevice and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "DeployedDevice";
		@SuppressWarnings("UnusedDeclaration")
		public static final String MACADDRESS = "MacAddress";
		@SuppressWarnings("UnusedDeclaration")
		public static final String MACPROTOCOL = "MacProtocol";
		@SuppressWarnings("UnusedDeclaration")
		public static final String METADATA = "Metadata";
		@SuppressWarnings("UnusedDeclaration")
		public static final String NETWORKINTERFACE = "NetworkInterface";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setMacAddress(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedDevice(), MACADDRESS, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearMacAddress(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedDevice(), MACADDRESS, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getMacAddress(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDeployedDevice(), MACADDRESS));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setMacProtocol(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedDevice(), MACPROTOCOL, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearMacProtocol(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedDevice(), MACPROTOCOL, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getMacProtocol(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDeployedDevice(), MACPROTOCOL));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setMetadata(Element element, java.util.List<Element> value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedDevice(), METADATA, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearMetadata(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedDevice(), METADATA, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void addMetadata(Element element, Element value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedDevice(), METADATA, value, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void removeMetadata(Element element, Element value) {
			java.util.List<Element> values = new java.util.ArrayList<>(getMetadata(element));
			values.remove(value);
			setMetadata(element, values);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@Nonnull
		public static java.util.List<Element> getMetadata(Element element) {
			return (java.util.List<Element>) (StereotypesHelper
				.getStereotypePropertyValue(element, getInstance(element).getDeployedDevice(), METADATA));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setNetworkInterface(Element element, Element value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedDevice(), NETWORKINTERFACE, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearNetworkInterface(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedDevice(), NETWORKINTERFACE, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Element getNetworkInterface(Element element) {
			return (Element) (StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDeployedDevice(), NETWORKINTERFACE));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getDeployedDevice() {
		return getStereotype(DeployedDevice.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isDeployedDevice(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getDeployedDevice());
		}
		return false;
	}
	
	public static class DeployedEntity extends AbstractStereotypeWrapper {
		
		//stereotype DeployedEntity and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "DeployedEntity";
		@SuppressWarnings("UnusedDeclaration")
		public static final String IDENTIFIER = "identifier";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setIdentifier(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedEntity(), IDENTIFIER, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearIdentifier(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedEntity(), IDENTIFIER, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getIdentifier(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDeployedEntity(), IDENTIFIER));
		}
		
	}
	
	/**
	 * The identifier of any deployed entity is a string which (ideally) uniquely identifies that given entity in an
	 * SoS.
	 * <p>
	 * For making the identifiers compatible with the rest of the Arrowhead ecosystem, it is highly advisable to use the
	 * following conventions:
	 * <p>
	 * System identifiers: _systemname._devicename._protocol._transport._domain
	 * <p>
	 * Device identifiers: _devicename._localcloudname._interface._mac?protocol._macaddress
	 * <p>
	 * Local cloud identifiers: _gatekeeper?system?name._InterCloudNegotiations. _protocol._transport._domain:port
	 *
	 * @return stereotype
	 */
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getDeployedEntity() {
		return getStereotype(DeployedEntity.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isDeployedEntity(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getDeployedEntity());
		}
		return false;
	}
	
	public static class DeployedLocalCloud extends AbstractStereotypeWrapper {
		
		//stereotype DeployedLocalCloud and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "DeployedLocalCloud";
		@SuppressWarnings("UnusedDeclaration")
		public static final String GATEKEEPERSYSTEMNAME = "GatekeeperSystemName";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setGatekeeperSystemName(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedLocalCloud(), GATEKEEPERSYSTEMNAME,
					value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearGatekeeperSystemName(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedLocalCloud(), GATEKEEPERSYSTEMNAME,
					true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getGatekeeperSystemName(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDeployedLocalCloud(),
					GATEKEEPERSYSTEMNAME));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getDeployedLocalCloud() {
		return getStereotype(DeployedLocalCloud.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isDeployedLocalCloud(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getDeployedLocalCloud());
		}
		return false;
	}
	
	public static class DeployedSystem extends AbstractStereotypeWrapper {
		
		//stereotype DeployedSystem and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "DeployedSystem";
		@SuppressWarnings("UnusedDeclaration")
		public static final String ADDRESS = "address";
		@SuppressWarnings("UnusedDeclaration")
		public static final String AUTHENTICATIONINFO = "authenticationInfo";
		@SuppressWarnings("UnusedDeclaration")
		public static final String METADATA = "metadata";
		@SuppressWarnings("UnusedDeclaration")
		public static final String PORT = "port";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setAddress(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedSystem(), ADDRESS, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearAddress(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getDeployedSystem(), ADDRESS, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getAddress(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDeployedSystem(), ADDRESS));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setAuthenticationInfo(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedSystem(), AUTHENTICATIONINFO,
					value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearAuthenticationInfo(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedSystem(), AUTHENTICATIONINFO, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getAuthenticationInfo(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDeployedSystem(), AUTHENTICATIONINFO));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setMetadata(Element element, java.util.List<Element> value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedSystem(), METADATA, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearMetadata(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getDeployedSystem(), METADATA, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void addMetadata(Element element, Element value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedSystem(), METADATA, value, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void removeMetadata(Element element, Element value) {
			java.util.List<Element> values = new java.util.ArrayList<>(getMetadata(element));
			values.remove(value);
			setMetadata(element, values);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@Nonnull
		public static java.util.List<Element> getMetadata(Element element) {
			return (java.util.List<Element>) (StereotypesHelper
				.getStereotypePropertyValue(element, getInstance(element).getDeployedSystem(), METADATA));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setPort(Element element, Integer value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getDeployedSystem(), PORT, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearPort(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getDeployedSystem(), PORT, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Integer getPort(Element element) {
			return toInteger(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getDeployedSystem(), PORT));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getDeployedSystem() {
		return getStereotype(DeployedSystem.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isDeployedSystem(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getDeployedSystem());
		}
		return false;
	}
	
	public static class Device extends AbstractStereotypeWrapper {
		
		//stereotype Device and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Device";
		@SuppressWarnings("UnusedDeclaration")
		public static final String LOCALCLOUD = "LocalCloud";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setLocalCloud(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getDevice(), LOCALCLOUD, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearLocalCloud(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getDevice(), LOCALCLOUD, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Element getLocalCloud(Element element) {
			return (Element) (StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDevice(), LOCALCLOUD));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getDevice() {
		return getStereotype(Device.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isDevice(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getDevice());
		}
		return false;
	}
	
	public static class IDD extends AbstractStereotypeWrapper {
		
		//stereotype IDD and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "IDD";
		@SuppressWarnings("UnusedDeclaration")
		public static final String ENCODING = "Encoding";
		@SuppressWarnings("UnusedDeclaration")
		public static final String PROTOCOL = "Protocol";
		@SuppressWarnings("UnusedDeclaration")
		public static final String SECURITY = "Security";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setEncoding(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getIDD(), ENCODING, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearEncoding(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getIDD(), ENCODING, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getEncoding(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getIDD(), ENCODING));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setProtocol(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getIDD(), PROTOCOL, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearProtocol(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getIDD(), PROTOCOL, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getProtocol(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getIDD(), PROTOCOL));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setSecurity(Element element, SecurityKindEnum value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getIDD(), SECURITY,
				value != null ? value.getText() : null);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearSecurity(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getIDD(), SECURITY, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static SecurityKindEnum getSecurity(Element element) {
			return SecurityKindEnum
				.from(StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getIDD(), SECURITY));
		}
		
	}
	
	/**
	 * Within an IDD, a service interface with input, output and return parameters can be modeled as operations.
	 *
	 * @return stereotype
	 */
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getIDD() {
		return getStereotype(IDD.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isIDD(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getIDD());
		}
		return false;
	}
	
	public static class LocalCloud extends AbstractStereotypeWrapper {
		
		//stereotype LocalCloud and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "LocalCloud";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DEVICES = "Devices";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setDevices(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getLocalCloud(), DEVICES, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearDevices(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getLocalCloud(), DEVICES, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Element getDevices(Element element) {
			return (Element) (StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getLocalCloud(), DEVICES));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getLocalCloud() {
		return getStereotype(LocalCloud.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isLocalCloud(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getLocalCloud());
		}
		return false;
	}
	
	public static class MetaDataEntry extends AbstractStereotypeWrapper {
		
		//stereotype MetaDataEntry and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "MetaDataEntry";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DATA = "data";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setData(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getMetaDataEntry(), DATA, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearData(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getMetaDataEntry(), DATA, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getData(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getMetaDataEntry(), DATA));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getMetaDataEntry() {
		return getStereotype(MetaDataEntry.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isMetaDataEntry(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getMetaDataEntry());
		}
		return false;
	}
	
	public static class SD extends AbstractStereotypeWrapper {
		
		//stereotype SD and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "SD";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getSD() {
		return getStereotype(SD.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isSD(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getSD());
		}
		return false;
	}
	
	public static class SoSD extends AbstractStereotypeWrapper {
		
		//stereotype SoSD and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "SoSD";
		@SuppressWarnings("UnusedDeclaration")
		public static final String SYSDS = "SysDs";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setSysDs(Element element, java.util.List<Element> value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSoSD(), SYSDS, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearSysDs(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getSoSD(), SYSDS, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void addSysDs(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSoSD(), SYSDS, value, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void removeSysDs(Element element, Element value) {
			java.util.List<Element> values = new java.util.ArrayList<>(getSysDs(element));
			values.remove(value);
			setSysDs(element, values);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@Nonnull
		public static java.util.List<Element> getSysDs(Element element) {
			return (java.util.List<Element>) (StereotypesHelper
				.getStereotypePropertyValue(element, getInstance(element).getSoSD(), SYSDS));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getSoSD() {
		return getStereotype(SoSD.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isSoSD(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getSoSD());
		}
		return false;
	}
	
	public static class SoSDD extends AbstractStereotypeWrapper {
		
		//stereotype SoSDD and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "SoSDD";
		@SuppressWarnings("UnusedDeclaration")
		public static final String SYSDDS = "SysDDs";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setSysDDs(Element element, java.util.List<Element> value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSoSDD(), SYSDDS, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearSysDDs(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getSoSDD(), SYSDDS, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void addSysDDs(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSoSDD(), SYSDDS, value, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void removeSysDDs(Element element, Element value) {
			java.util.List<Element> values = new java.util.ArrayList<>(getSysDDs(element));
			values.remove(value);
			setSysDDs(element, values);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@Nonnull
		public static java.util.List<Element> getSysDDs(Element element) {
			return (java.util.List<Element>) (StereotypesHelper
				.getStereotypePropertyValue(element, getInstance(element).getSoSDD(), SYSDDS));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getSoSDD() {
		return getStereotype(SoSDD.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isSoSDD(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getSoSDD());
		}
		return false;
	}
	
	public static class SubsetOf extends AbstractStereotypeWrapper {
		
		//stereotype SubsetOf and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "SubsetOf";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getSubsetOf() {
		return getStereotype(SubsetOf.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isSubsetOf(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Dependency) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getSubsetOf());
		}
		return false;
	}
	
	public static class SysD extends AbstractStereotypeWrapper {
		
		//stereotype SysD and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "SysD";
		@SuppressWarnings("UnusedDeclaration")
		public static final String SOSDS = "SoSDs";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setSoSDs(Element element, java.util.List<Element> value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSysD(), SOSDS, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearSoSDs(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getSysD(), SOSDS, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void addSoSDs(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSysD(), SOSDS, value, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void removeSoSDs(Element element, Element value) {
			java.util.List<Element> values = new java.util.ArrayList<>(getSoSDs(element));
			values.remove(value);
			setSoSDs(element, values);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@Nonnull
		public static java.util.List<Element> getSoSDs(Element element) {
			return (java.util.List<Element>) (StereotypesHelper
				.getStereotypePropertyValue(element, getInstance(element).getSysD(), SOSDS));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getSysD() {
		return getStereotype(SysD.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isSysD(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getSysD());
		}
		return false;
	}
	
	public static class SysDD extends AbstractStereotypeWrapper {
		
		//stereotype SysDD and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "SysDD";
		@SuppressWarnings("UnusedDeclaration")
		public static final String SOSDDS = "SoSDDs";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setSoSDDs(Element element, java.util.List<Element> value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSysDD(), SOSDDS, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearSoSDDs(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getSysDD(), SOSDDS, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void addSoSDDs(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getSysDD(), SOSDDS, value, true);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void removeSoSDDs(Element element, Element value) {
			java.util.List<Element> values = new java.util.ArrayList<>(getSoSDDs(element));
			values.remove(value);
			setSoSDDs(element, values);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@Nonnull
		public static java.util.List<Element> getSoSDDs(Element element) {
			return (java.util.List<Element>) (StereotypesHelper
				.getStereotypePropertyValue(element, getInstance(element).getSysDD(), SOSDDS));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getSysDD() {
		return getStereotype(SysDD.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isSysDD(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			ArrowheadProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getSysDD());
		}
		return false;
	}
	
	@Override
	protected Collection<Stereotype> generatedGetAllStereotypes() {
		if(getProfile() != null) {
			final Collection<Stereotype> stereotypes = new HashSet<>();
			
			stereotypes.add(getDataSemantics());
			stereotypes.add(getDeployedDevice());
			stereotypes.add(getDeployedEntity());
			stereotypes.add(getDeployedLocalCloud());
			stereotypes.add(getDeployedSystem());
			stereotypes.add(getDevice());
			stereotypes.add(getIDD());
			stereotypes.add(getLocalCloud());
			stereotypes.add(getMetaDataEntry());
			stereotypes.add(getSD());
			stereotypes.add(getSoSD());
			stereotypes.add(getSoSDD());
			stereotypes.add(getSubsetOf());
			stereotypes.add(getSysD());
			stereotypes.add(getSysDD());
			
			return stereotypes;
		}
		
		return super.generatedGetAllStereotypes();
	}
	
}
//MD5sum:D3E6ADDC261120489ACF43D1A1B80393