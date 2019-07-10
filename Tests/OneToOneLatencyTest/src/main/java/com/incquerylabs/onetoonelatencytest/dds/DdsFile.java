package com.incquerylabs.onetoonelatencytest.dds;

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from .idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

import com.rti.dds.infrastructure.*;
import com.rti.dds.infrastructure.Copyable;
import java.io.Serializable;
import com.rti.dds.cdr.CdrHelper;

@SuppressWarnings({ "unused", "serial" })
public class DdsFile implements Copyable, Serializable {

	public String File = "";

	public DdsFile() {

	}

	public DdsFile(DdsFile other) {

		this();
		copy_from(other);
	}

	public static Object create() {

		DdsFile self;
		self = new DdsFile();
		self.clear();
		return self;

	}

	public void clear() {

		File = "";
	}

	public boolean equals(Object o) {

		if (o == null) {
			return false;
		}

		if (getClass() != o.getClass()) {
			return false;
		}

		DdsFile otherObj = (DdsFile) o;

		if (!File.equals(otherObj.File)) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int __result = 0;
		__result += File.hashCode();
		return __result;
	}

	/**
	 * This is the implementation of the <code>Copyable</code> interface. This
	 * method will perform a deep copy of <code>src</code> This method could be
	 * placed into <code>DdsFileTypeSupport</code> rather than here by using the
	 * <code>-noCopyable</code> option to rtiddsgen.
	 * 
	 * @param src The Object which contains the data to be copied.
	 * @return Returns <code>this</code>.
	 * @exception NullPointerException If <code>src</code> is null.
	 * @exception ClassCastException   If <code>src</code> is not the same type as
	 *                                 <code>this</code>.
	 * @see com.rti.dds.infrastructure.Copyable#copy_from(java.lang.Object)
	 */
	public Object copy_from(Object src) {

		DdsFile typedSrc = (DdsFile) src;
		DdsFile typedDst = this;

		typedDst.File = typedSrc.File;

		return this;
	}

	public String toString() {
		return toString("", 0);
	}

	public String toString(String desc, int indent) {
		StringBuffer strBuffer = new StringBuffer();

		if (desc != null) {
			CdrHelper.printIndent(strBuffer, indent);
			strBuffer.append(desc).append(":\n");
		}

		CdrHelper.printIndent(strBuffer, indent + 1);
		strBuffer.append("File: ").append(File).append("\n");

		return strBuffer.toString();
	}

}
