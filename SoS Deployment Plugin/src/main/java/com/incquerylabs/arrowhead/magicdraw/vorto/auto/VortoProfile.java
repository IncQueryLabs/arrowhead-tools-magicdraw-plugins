package com.incquerylabs.arrowhead.magicdraw.vorto.auto;

import com.nomagic.uml2.ext.jmi.helpers.StereotypeByProfileCache;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.uml.BaseElement;

import java.util.Collection;
import java.util.HashSet;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@SuppressWarnings("all")
public class VortoProfile extends StereotypeByProfileCache {
	
	public static final String PROFILE_URI = "";
	
	public static final String PROFILE_NAME = "Arrowhead Vorto Extension";
	
	public static VortoProfile getInstance(@Nonnull BaseElement element) {
		return getInstance(Project.getProject(element));
	}
	
	public VortoProfile(@Nonnull Project project) {
		super(project, PROFILE_NAME, PROFILE_URI);
	}
	
	public static VortoProfile getInstance(@Nonnull Project project) {
		VortoProfile instance = getInternalInstance(VortoProfile.class, project);
		if(instance == null) {
			instance = new VortoProfile(project);
		}
		return instance;
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static final String BASE64_DATATYPE = "Base64";
	@SuppressWarnings("UnusedDeclaration")
	public static final String BYTE_DATATYPE = "Byte";
	@SuppressWarnings("UnusedDeclaration")
	public static final String DATETIME_DATATYPE = "DateTime";
	@SuppressWarnings("UnusedDeclaration")
	public static final String DOUBLE_DATATYPE = "Double";
	@SuppressWarnings("UnusedDeclaration")
	public static final String FLOAT_DATATYPE = "Float";
	@SuppressWarnings("UnusedDeclaration")
	public static final String INTEGER_DATATYPE = "Integer";
	@SuppressWarnings("UnusedDeclaration")
	public static final String LONG_DATATYPE = "Long";
	@SuppressWarnings("UnusedDeclaration")
	public static final String SHORT_DATATYPE = "Short";
	@SuppressWarnings("UnusedDeclaration")
	public static final String VORTO_VERSION_DATATYPE = "Vorto Version";
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getBase64() {
		return (DataType) getDataType(BASE64_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getByte() {
		return (DataType) getDataType(BYTE_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getDateTime() {
		return (DataType) getDataType(DATETIME_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getDouble() {
		return (DataType) getDataType(DOUBLE_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getFloat() {
		return (DataType) getDataType(FLOAT_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getInteger() {
		return (DataType) getDataType(INTEGER_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getLong() {
		return (DataType) getDataType(LONG_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public DataType getShort() {
		return (DataType) getDataType(SHORT_DATATYPE);
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Enumeration getVortoVersion() {
		return (Enumeration) getDataType(VORTO_VERSION_DATATYPE);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static final String VORTO_VERSION_1_0_LITERAL = "1.0";
	
	public enum VortoVersionEnum {
		_1_0(VORTO_VERSION_1_0_LITERAL);
		private String text;
		
		VortoVersionEnum(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		@CheckForNull
		public static VortoVersionEnum from(@CheckForNull Object o) {
			String text = null;
			if(o instanceof EnumerationLiteral) {
				text = ((EnumerationLiteral) o).getName();
			} else if(o != null) {
				text = o.toString();
			}
			if(text != null) {
				for(VortoVersionEnum b : VortoVersionEnum.values()) {
					if(text.equals(b.text)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	
	public static class Contains extends AbstractStereotypeWrapper {
		
		//stereotype Contains and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Contains";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DESCRIPTION = "description";
		@SuppressWarnings("UnusedDeclaration")
		public static final String IDENTIFIER = "identifier";
		@SuppressWarnings("UnusedDeclaration")
		public static final String MANDATORY = "mandatory";
		@SuppressWarnings("UnusedDeclaration")
		public static final String MULTIPLE = "multiple";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setDescription(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getContains(), DESCRIPTION, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearDescription(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getContains(), DESCRIPTION, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getDescription(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getContains(), DESCRIPTION));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setIdentifier(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getContains(), IDENTIFIER, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearIdentifier(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getContains(), IDENTIFIER, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getIdentifier(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getContains(), IDENTIFIER));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setMandatory(Element element, Boolean value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getContains(), MANDATORY, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearMandatory(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getContains(), MANDATORY, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Boolean isMandatory(Element element) {
			return toBoolean(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getContains(), MANDATORY));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setMultiple(Element element, Boolean value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getContains(), MULTIPLE, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearMultiple(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getContains(), MULTIPLE, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Boolean isMultiple(Element element) {
			return toBoolean(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getContains(), MULTIPLE));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getContains() {
		return getStereotype(Contains.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isContains(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Dependency) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getContains());
		}
		return false;
	}
	
	public static class Dictionary extends AbstractStereotypeWrapper {
		
		//stereotype Dictionary and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Dictionary";
		@SuppressWarnings("UnusedDeclaration")
		public static final String KEY = "key";
		@SuppressWarnings("UnusedDeclaration")
		public static final String VALUE = "value";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setKey(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getDictionary(), KEY, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearKey(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getDictionary(), KEY, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Element getKey(Element element) {
			return (Element) (StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDictionary(), KEY));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setValue(Element element, Element value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getDictionary(), VALUE, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearValue(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getDictionary(), VALUE, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Element getValue(Element element) {
			return (Element) (StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getDictionary(), VALUE));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getDictionary() {
		return getStereotype(Dictionary.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isDictionary(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getDictionary());
		}
		return false;
	}
	
	public static class Event extends AbstractStereotypeWrapper {
		
		//stereotype Event and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Event";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DESCRIPTION = "description";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setDescription(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getEvent(), DESCRIPTION, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearDescription(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getEvent(), DESCRIPTION, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getDescription(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getEvent(), DESCRIPTION));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getEvent() {
		return getStereotype(Event.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isEvent(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getEvent());
		}
		return false;
	}
	
	public static class FunctionBlock extends AbstractStereotypeWrapper {
		
		//stereotype Function Block and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Function Block";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getFunctionBlock() {
		return getStereotype(FunctionBlock.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isFunctionBlock(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getFunctionBlock());
		}
		return false;
	}
	
	public static class InformationModel extends AbstractStereotypeWrapper {
		
		//stereotype Information Model and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Information Model";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getInformationModel() {
		return getStereotype(InformationModel.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isInformationModel(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getInformationModel());
		}
		return false;
	}
	
	public static class Model extends AbstractStereotypeWrapper {
		
		//stereotype Model and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Model";
		@SuppressWarnings("UnusedDeclaration")
		public static final String CATEGORY = "category";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DESCRIPTION = "description";
		@SuppressWarnings("UnusedDeclaration")
		public static final String IDENTIFIER = "identifier";
		@SuppressWarnings("UnusedDeclaration")
		public static final String NAMESPACE = "namespace";
		@SuppressWarnings("UnusedDeclaration")
		public static final String VERSION = "version";
		@SuppressWarnings("UnusedDeclaration")
		public static final String VORTOLANG = "vortolang";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setCategory(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getModel(), CATEGORY, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearCategory(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getModel(), CATEGORY, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getCategory(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getModel(), CATEGORY));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setDescription(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getModel(), DESCRIPTION, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearDescription(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getModel(), DESCRIPTION, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getDescription(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getModel(), DESCRIPTION));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setIdentifier(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getModel(), IDENTIFIER, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearIdentifier(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getModel(), IDENTIFIER, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getIdentifier(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getModel(), IDENTIFIER));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setNamespace(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getModel(), NAMESPACE, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearNamespace(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getModel(), NAMESPACE, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getNamespace(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getModel(), NAMESPACE));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setVersion(Element element, String value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getModel(), VERSION, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearVersion(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getModel(), VERSION, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getVersion(Element element) {
			return toString(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getModel(), VERSION));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setVortolang(Element element, VortoVersionEnum value) {
			StereotypesHelper.setStereotypePropertyValue(element, getInstance(element).getModel(), VORTOLANG,
				value != null ? value.getText() : null);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearVortolang(Element element) {
			StereotypesHelper.clearStereotypeProperty(element, getInstance(element).getModel(), VORTOLANG, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static VortoVersionEnum getVortolang(Element element) {
			return VortoVersionEnum.from(
				StereotypesHelper.getStereotypePropertyFirst(element, getInstance(element).getModel(), VORTOLANG));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getModel() {
		return getStereotype(Model.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isModel(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getModel());
		}
		return false;
	}
	
	public static class VortoConfiguration extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Configuration and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Configuration";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoConfiguration() {
		return getStereotype(VortoConfiguration.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoConfiguration(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoConfiguration());
		}
		return false;
	}
	
	public static class VortoDevice extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Device and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Device";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoDevice() {
		return getStereotype(VortoDevice.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoDevice(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoDevice());
		}
		return false;
	}
	
	public static class VortoEntity extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Entity and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Entity";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoEntity() {
		return getStereotype(VortoEntity.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoEntity(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoEntity());
		}
		return false;
	}
	
	public static class VortoEnumeration extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Enumeration and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Enumeration";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoEnumeration() {
		return getStereotype(VortoEnumeration.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoEnumeration(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoEnumeration());
		}
		return false;
	}
	
	public static class VortoIDD extends AbstractStereotypeWrapper {
		
		//stereotype Vorto IDD and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto IDD";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoIDD() {
		return getStereotype(VortoIDD.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoIDD(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoIDD());
		}
		return false;
	}
	
	public static class VortoLiteral extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Literal and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Literal";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DESCRIPTION = "description";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setDescription(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getVortoLiteral(), DESCRIPTION, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearDescription(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getVortoLiteral(), DESCRIPTION, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getDescription(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getVortoLiteral(), DESCRIPTION));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoLiteral() {
		return getStereotype(VortoLiteral.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoLiteral(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoLiteral());
		}
		return false;
	}
	
	public static class VortoOperation extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Operation and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Operation";
		@SuppressWarnings("UnusedDeclaration")
		public static final String BREAKABLE = "breakable";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DESCRIPTION = "description";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setBreakable(Element element, Boolean value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getVortoOperation(), BREAKABLE, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearBreakable(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getVortoOperation(), BREAKABLE, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static Boolean isBreakable(Element element) {
			return toBoolean(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getVortoOperation(), BREAKABLE));
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setDescription(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getVortoOperation(), DESCRIPTION, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearDescription(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getVortoOperation(), DESCRIPTION, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getDescription(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getVortoOperation(), DESCRIPTION));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoOperation() {
		return getStereotype(VortoOperation.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoOperation(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Operation) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoOperation());
		}
		return false;
	}
	
	public static class VortoProperty extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Property and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Property";
		@SuppressWarnings("UnusedDeclaration")
		public static final String DESCRIPTION = "description";
		
		@SuppressWarnings("UnusedDeclaration")
		public static void setDescription(Element element, String value) {
			StereotypesHelper
				.setStereotypePropertyValue(element, getInstance(element).getVortoProperty(), DESCRIPTION, value);
		}
		
		@SuppressWarnings("UnusedDeclaration")
		public static void clearDescription(Element element) {
			StereotypesHelper
				.clearStereotypeProperty(element, getInstance(element).getVortoProperty(), DESCRIPTION, true);
		}
		
		@SuppressWarnings("UnusedDeclaration, unchecked")
		@CheckForNull
		public static String getDescription(Element element) {
			return toString(StereotypesHelper
				.getStereotypePropertyFirst(element, getInstance(element).getVortoProperty(), DESCRIPTION));
		}
		
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoProperty() {
		return getStereotype(VortoProperty.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoProperty(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoProperty());
		}
		return false;
	}
	
	public static class VortoStatus extends AbstractStereotypeWrapper {
		
		//stereotype Vorto Status and its tags
		@SuppressWarnings("UnusedDeclaration")
		public static final String STEREOTYPE_NAME = "Vorto Status";
	}
	
	@SuppressWarnings({"UnusedDeclaration", "ConstantConditions"})
	public Stereotype getVortoStatus() {
		return getStereotype(VortoStatus.STEREOTYPE_NAME);
	}
	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean isVortoStatus(@CheckForNull Element element) {
		if(element instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property) {
			VortoProfile instance = getInstance(element);
			return instance.isTypeOf(element, instance.getVortoStatus());
		}
		return false;
	}
	
	@Override
	protected Collection<Stereotype> generatedGetAllStereotypes() {
		if(getProfile() != null) {
			final Collection<Stereotype> stereotypes = new HashSet<>();
			
			stereotypes.add(getContains());
			stereotypes.add(getDictionary());
			stereotypes.add(getEvent());
			stereotypes.add(getFunctionBlock());
			stereotypes.add(getInformationModel());
			stereotypes.add(getModel());
			stereotypes.add(getVortoConfiguration());
			stereotypes.add(getVortoDevice());
			stereotypes.add(getVortoEntity());
			stereotypes.add(getVortoEnumeration());
			stereotypes.add(getVortoIDD());
			stereotypes.add(getVortoLiteral());
			stereotypes.add(getVortoOperation());
			stereotypes.add(getVortoProperty());
			stereotypes.add(getVortoStatus());
			
			return stereotypes;
		}
		
		return super.generatedGetAllStereotypes();
	}
	
}
//MD5sum:B1978238511DF9B2CBF49A3024F2031D