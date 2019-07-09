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

@SuppressWarnings("serial")
public class FileChunkType   implements Copyable, Serializable{

    public String filename=  "" ; /* maximum length = (1024) */
    public ByteSeq data =  new ByteSeq(63308);

    public FileChunkType() {

    }
    public FileChunkType (FileChunkType other) {

        this();
        copy_from(other);
    }

    public static Object create() {

        FileChunkType self;
        self = new  FileChunkType();
        self.clear();
        return self;

    }

    public void clear() {

        filename=  ""; 
        if (data != null) {
            data.clear();
        }
    }

    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }        

        if(getClass() != o.getClass()) {
            return false;
        }

        FileChunkType otherObj = (FileChunkType)o;

        if(!filename.equals(otherObj.filename)) {
            return false;
        }
        if(!data.equals(otherObj.data)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int __result = 0;
        __result += filename.hashCode(); 
        __result += data.hashCode(); 
        return __result;
    }

    /**
    * This is the implementation of the <code>Copyable</code> interface.
    * This method will perform a deep copy of <code>src</code>
    * This method could be placed into <code>FileChunkTypeTypeSupport</code>
    * rather than here by using the <code>-noCopyable</code> option
    * to rtiddsgen.
    * 
    * @param src The Object which contains the data to be copied.
    * @return Returns <code>this</code>.
    * @exception NullPointerException If <code>src</code> is null.
    * @exception ClassCastException If <code>src</code> is not the 
    * same type as <code>this</code>.
    * @see com.rti.dds.infrastructure.Copyable#copy_from(java.lang.Object)
    */
    public Object copy_from(Object src) {

        FileChunkType typedSrc = (FileChunkType) src;
        FileChunkType typedDst = this;

        typedDst.filename = typedSrc.filename;
        typedDst.data.copy_from(typedSrc.data);

        return this;
    }

    public String toString(){
        return toString("", 0);
    }

    public String toString(String desc, int indent) {
        StringBuffer strBuffer = new StringBuffer();        

        if (desc != null) {
            CdrHelper.printIndent(strBuffer, indent);
            strBuffer.append(desc).append(":\n");
        }

        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("filename: ").append(filename).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);
        strBuffer.append("data: ");
        for(int i__ = 0; i__ < data.size(); ++i__) {
            if (i__!=0) strBuffer.append(", ");
            strBuffer.append(data.get(i__));
        }
        strBuffer.append("\n"); 

        return strBuffer.toString();
    }

}
