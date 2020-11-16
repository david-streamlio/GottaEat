/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.gottaeat.domain.fraud.scoring.fraudlabs;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class Address extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -878416431704489417L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Address\",\"namespace\":\"com.gottaeat.domain.fraud.scoring.fraudlabs\",\"fields\":[{\"name\":\"address\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"state\",\"type\":\"string\"},{\"name\":\"country\",\"type\":\"string\"},{\"name\":\"postalCode\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Address> ENCODER =
      new BinaryMessageEncoder<Address>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Address> DECODER =
      new BinaryMessageDecoder<Address>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Address> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Address> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Address> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Address>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Address to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Address from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Address instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Address fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence address;
  @Deprecated public java.lang.CharSequence city;
  @Deprecated public java.lang.CharSequence state;
  @Deprecated public java.lang.CharSequence country;
  @Deprecated public java.lang.CharSequence postalCode;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Address() {}

  /**
   * All-args constructor.
   * @param address The new value for address
   * @param city The new value for city
   * @param state The new value for state
   * @param country The new value for country
   * @param postalCode The new value for postalCode
   */
  public Address(java.lang.CharSequence address, java.lang.CharSequence city, java.lang.CharSequence state, java.lang.CharSequence country, java.lang.CharSequence postalCode) {
    this.address = address;
    this.city = city;
    this.state = state;
    this.country = country;
    this.postalCode = postalCode;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return address;
    case 1: return city;
    case 2: return state;
    case 3: return country;
    case 4: return postalCode;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: address = (java.lang.CharSequence)value$; break;
    case 1: city = (java.lang.CharSequence)value$; break;
    case 2: state = (java.lang.CharSequence)value$; break;
    case 3: country = (java.lang.CharSequence)value$; break;
    case 4: postalCode = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'address' field.
   * @return The value of the 'address' field.
   */
  public java.lang.CharSequence getAddress() {
    return address;
  }


  /**
   * Sets the value of the 'address' field.
   * @param value the value to set.
   */
  public void setAddress(java.lang.CharSequence value) {
    this.address = value;
  }

  /**
   * Gets the value of the 'city' field.
   * @return The value of the 'city' field.
   */
  public java.lang.CharSequence getCity() {
    return city;
  }


  /**
   * Sets the value of the 'city' field.
   * @param value the value to set.
   */
  public void setCity(java.lang.CharSequence value) {
    this.city = value;
  }

  /**
   * Gets the value of the 'state' field.
   * @return The value of the 'state' field.
   */
  public java.lang.CharSequence getState() {
    return state;
  }


  /**
   * Sets the value of the 'state' field.
   * @param value the value to set.
   */
  public void setState(java.lang.CharSequence value) {
    this.state = value;
  }

  /**
   * Gets the value of the 'country' field.
   * @return The value of the 'country' field.
   */
  public java.lang.CharSequence getCountry() {
    return country;
  }


  /**
   * Sets the value of the 'country' field.
   * @param value the value to set.
   */
  public void setCountry(java.lang.CharSequence value) {
    this.country = value;
  }

  /**
   * Gets the value of the 'postalCode' field.
   * @return The value of the 'postalCode' field.
   */
  public java.lang.CharSequence getPostalCode() {
    return postalCode;
  }


  /**
   * Sets the value of the 'postalCode' field.
   * @param value the value to set.
   */
  public void setPostalCode(java.lang.CharSequence value) {
    this.postalCode = value;
  }

  /**
   * Creates a new Address RecordBuilder.
   * @return A new Address RecordBuilder
   */
  public static com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder newBuilder() {
    return new com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder();
  }

  /**
   * Creates a new Address RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Address RecordBuilder
   */
  public static com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder newBuilder(com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder other) {
    if (other == null) {
      return new com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder();
    } else {
      return new com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder(other);
    }
  }

  /**
   * Creates a new Address RecordBuilder by copying an existing Address instance.
   * @param other The existing instance to copy.
   * @return A new Address RecordBuilder
   */
  public static com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder newBuilder(com.gottaeat.domain.fraud.scoring.fraudlabs.Address other) {
    if (other == null) {
      return new com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder();
    } else {
      return new com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder(other);
    }
  }

  /**
   * RecordBuilder for Address instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Address>
    implements org.apache.avro.data.RecordBuilder<Address> {

    private java.lang.CharSequence address;
    private java.lang.CharSequence city;
    private java.lang.CharSequence state;
    private java.lang.CharSequence country;
    private java.lang.CharSequence postalCode;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.address)) {
        this.address = data().deepCopy(fields()[0].schema(), other.address);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.city)) {
        this.city = data().deepCopy(fields()[1].schema(), other.city);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.state)) {
        this.state = data().deepCopy(fields()[2].schema(), other.state);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.country)) {
        this.country = data().deepCopy(fields()[3].schema(), other.country);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.postalCode)) {
        this.postalCode = data().deepCopy(fields()[4].schema(), other.postalCode);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
    }

    /**
     * Creates a Builder by copying an existing Address instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gottaeat.domain.fraud.scoring.fraudlabs.Address other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.address)) {
        this.address = data().deepCopy(fields()[0].schema(), other.address);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.city)) {
        this.city = data().deepCopy(fields()[1].schema(), other.city);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.state)) {
        this.state = data().deepCopy(fields()[2].schema(), other.state);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.country)) {
        this.country = data().deepCopy(fields()[3].schema(), other.country);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.postalCode)) {
        this.postalCode = data().deepCopy(fields()[4].schema(), other.postalCode);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'address' field.
      * @return The value.
      */
    public java.lang.CharSequence getAddress() {
      return address;
    }


    /**
      * Sets the value of the 'address' field.
      * @param value The value of 'address'.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder setAddress(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.address = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'address' field has been set.
      * @return True if the 'address' field has been set, false otherwise.
      */
    public boolean hasAddress() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'address' field.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder clearAddress() {
      address = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'city' field.
      * @return The value.
      */
    public java.lang.CharSequence getCity() {
      return city;
    }


    /**
      * Sets the value of the 'city' field.
      * @param value The value of 'city'.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder setCity(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.city = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'city' field has been set.
      * @return True if the 'city' field has been set, false otherwise.
      */
    public boolean hasCity() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'city' field.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder clearCity() {
      city = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'state' field.
      * @return The value.
      */
    public java.lang.CharSequence getState() {
      return state;
    }


    /**
      * Sets the value of the 'state' field.
      * @param value The value of 'state'.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder setState(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.state = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'state' field has been set.
      * @return True if the 'state' field has been set, false otherwise.
      */
    public boolean hasState() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'state' field.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder clearState() {
      state = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'country' field.
      * @return The value.
      */
    public java.lang.CharSequence getCountry() {
      return country;
    }


    /**
      * Sets the value of the 'country' field.
      * @param value The value of 'country'.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder setCountry(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.country = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'country' field has been set.
      * @return True if the 'country' field has been set, false otherwise.
      */
    public boolean hasCountry() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'country' field.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder clearCountry() {
      country = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'postalCode' field.
      * @return The value.
      */
    public java.lang.CharSequence getPostalCode() {
      return postalCode;
    }


    /**
      * Sets the value of the 'postalCode' field.
      * @param value The value of 'postalCode'.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder setPostalCode(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.postalCode = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'postalCode' field has been set.
      * @return True if the 'postalCode' field has been set, false otherwise.
      */
    public boolean hasPostalCode() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'postalCode' field.
      * @return This builder.
      */
    public com.gottaeat.domain.fraud.scoring.fraudlabs.Address.Builder clearPostalCode() {
      postalCode = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Address build() {
      try {
        Address record = new Address();
        record.address = fieldSetFlags()[0] ? this.address : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.city = fieldSetFlags()[1] ? this.city : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.state = fieldSetFlags()[2] ? this.state : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.country = fieldSetFlags()[3] ? this.country : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.postalCode = fieldSetFlags()[4] ? this.postalCode : (java.lang.CharSequence) defaultValue(fields()[4]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Address>
    WRITER$ = (org.apache.avro.io.DatumWriter<Address>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Address>
    READER$ = (org.apache.avro.io.DatumReader<Address>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.address);

    out.writeString(this.city);

    out.writeString(this.state);

    out.writeString(this.country);

    out.writeString(this.postalCode);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.address = in.readString(this.address instanceof Utf8 ? (Utf8)this.address : null);

      this.city = in.readString(this.city instanceof Utf8 ? (Utf8)this.city : null);

      this.state = in.readString(this.state instanceof Utf8 ? (Utf8)this.state : null);

      this.country = in.readString(this.country instanceof Utf8 ? (Utf8)this.country : null);

      this.postalCode = in.readString(this.postalCode instanceof Utf8 ? (Utf8)this.postalCode : null);

    } else {
      for (int i = 0; i < 5; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.address = in.readString(this.address instanceof Utf8 ? (Utf8)this.address : null);
          break;

        case 1:
          this.city = in.readString(this.city instanceof Utf8 ? (Utf8)this.city : null);
          break;

        case 2:
          this.state = in.readString(this.state instanceof Utf8 ? (Utf8)this.state : null);
          break;

        case 3:
          this.country = in.readString(this.country instanceof Utf8 ? (Utf8)this.country : null);
          break;

        case 4:
          this.postalCode = in.readString(this.postalCode instanceof Utf8 ? (Utf8)this.postalCode : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










