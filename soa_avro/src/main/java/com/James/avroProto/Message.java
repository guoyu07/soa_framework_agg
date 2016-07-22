/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.James.avroProto;

import org.apache.avro.specific.SpecificData;

/*
 * 根据avpr自动生成
 *
 */

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Message extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 7012675389317148321L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Message\",\"namespace\":\"com.James.avroProto\",\"fields\":[{\"name\":\"param\",\"type\":\"string\"},{\"name\":\"RequestName\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public CharSequence param;
  @Deprecated public CharSequence RequestName;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Message() {}

  /**
   * All-args constructor.
   * @param param The new value for param
   * @param RequestName The new value for RequestName
   */
  public Message(CharSequence param, CharSequence RequestName) {
    this.param = param;
    this.RequestName = RequestName;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public Object get(int field$) {
    switch (field$) {
    case 0: return param;
    case 1: return RequestName;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: param = (CharSequence)value$; break;
    case 1: RequestName = (CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'param' field.
   * @return The value of the 'param' field.
   */
  public CharSequence getParam() {
    return param;
  }

  /**
   * Sets the value of the 'param' field.
   * @param value the value to set.
   */
  public void setParam(CharSequence value) {
    this.param = value;
  }

  /**
   * Gets the value of the 'RequestName' field.
   * @return The value of the 'RequestName' field.
   */
  public CharSequence getRequestName() {
    return RequestName;
  }

  /**
   * Sets the value of the 'RequestName' field.
   * @param value the value to set.
   */
  public void setRequestName(CharSequence value) {
    this.RequestName = value;
  }

  /**
   * Creates a new Message RecordBuilder.
   * @return A new Message RecordBuilder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Creates a new Message RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Message RecordBuilder
   */
  public static Builder newBuilder(Builder other) {
    return new Builder(other);
  }

  /**
   * Creates a new Message RecordBuilder by copying an existing Message instance.
   * @param other The existing instance to copy.
   * @return A new Message RecordBuilder
   */
  public static Builder newBuilder(Message other) {
    return new Builder(other);
  }

  /**
   * RecordBuilder for Message instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Message>
    implements org.apache.avro.data.RecordBuilder<Message> {

    private CharSequence param;
    private CharSequence RequestName;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.param)) {
        this.param = data().deepCopy(fields()[0].schema(), other.param);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.RequestName)) {
        this.RequestName = data().deepCopy(fields()[1].schema(), other.RequestName);
        fieldSetFlags()[1] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Message instance
     * @param other The existing instance to copy.
     */
    private Builder(Message other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.param)) {
        this.param = data().deepCopy(fields()[0].schema(), other.param);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.RequestName)) {
        this.RequestName = data().deepCopy(fields()[1].schema(), other.RequestName);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'param' field.
      * @return The value.
      */
    public CharSequence getParam() {
      return param;
    }

    /**
      * Sets the value of the 'param' field.
      * @param value The value of 'param'.
      * @return This builder.
      */
    public Builder setParam(CharSequence value) {
      validate(fields()[0], value);
      this.param = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'param' field has been set.
      * @return True if the 'param' field has been set, false otherwise.
      */
    public boolean hasParam() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'param' field.
      * @return This builder.
      */
    public Builder clearParam() {
      param = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'RequestName' field.
      * @return The value.
      */
    public CharSequence getRequestName() {
      return RequestName;
    }

    /**
      * Sets the value of the 'RequestName' field.
      * @param value The value of 'RequestName'.
      * @return This builder.
      */
    public Builder setRequestName(CharSequence value) {
      validate(fields()[1], value);
      this.RequestName = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'RequestName' field has been set.
      * @return True if the 'RequestName' field has been set, false otherwise.
      */
    public boolean hasRequestName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'RequestName' field.
      * @return This builder.
      */
    public Builder clearRequestName() {
      RequestName = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public Message build() {
      try {
        Message record = new Message();
        record.param = fieldSetFlags()[0] ? this.param : (CharSequence) defaultValue(fields()[0]);
        record.RequestName = fieldSetFlags()[1] ? this.RequestName : (CharSequence) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
