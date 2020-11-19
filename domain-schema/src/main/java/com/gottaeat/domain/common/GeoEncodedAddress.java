/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.gottaeat.domain.common;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class GeoEncodedAddress extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2121716906377099323L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"GeoEncodedAddress\",\"namespace\":\"com.gottaeat.domain.common\",\"fields\":[{\"name\":\"address\",\"type\":{\"type\":\"record\",\"name\":\"Address\",\"fields\":[{\"name\":\"street\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"state\",\"type\":\"string\"},{\"name\":\"country\",\"type\":\"string\"},{\"name\":\"zip\",\"type\":\"string\"}]}},{\"name\":\"geo\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"LatLon\",\"fields\":[{\"name\":\"latitude\",\"type\":\"double\"},{\"name\":\"longitude\",\"type\":\"double\"}]}]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<GeoEncodedAddress> ENCODER =
      new BinaryMessageEncoder<GeoEncodedAddress>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<GeoEncodedAddress> DECODER =
      new BinaryMessageDecoder<GeoEncodedAddress>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<GeoEncodedAddress> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<GeoEncodedAddress> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<GeoEncodedAddress> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<GeoEncodedAddress>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this GeoEncodedAddress to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a GeoEncodedAddress from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a GeoEncodedAddress instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static GeoEncodedAddress fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public com.gottaeat.domain.common.Address address;
  @Deprecated public com.gottaeat.domain.common.LatLon geo;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public GeoEncodedAddress() {}

  /**
   * All-args constructor.
   * @param address The new value for address
   * @param geo The new value for geo
   */
  public GeoEncodedAddress(com.gottaeat.domain.common.Address address, com.gottaeat.domain.common.LatLon geo) {
    this.address = address;
    this.geo = geo;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return address;
    case 1: return geo;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: address = (com.gottaeat.domain.common.Address)value$; break;
    case 1: geo = (com.gottaeat.domain.common.LatLon)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'address' field.
   * @return The value of the 'address' field.
   */
  public com.gottaeat.domain.common.Address getAddress() {
    return address;
  }


  /**
   * Sets the value of the 'address' field.
   * @param value the value to set.
   */
  public void setAddress(com.gottaeat.domain.common.Address value) {
    this.address = value;
  }

  /**
   * Gets the value of the 'geo' field.
   * @return The value of the 'geo' field.
   */
  public com.gottaeat.domain.common.LatLon getGeo() {
    return geo;
  }


  /**
   * Sets the value of the 'geo' field.
   * @param value the value to set.
   */
  public void setGeo(com.gottaeat.domain.common.LatLon value) {
    this.geo = value;
  }

  /**
   * Creates a new GeoEncodedAddress RecordBuilder.
   * @return A new GeoEncodedAddress RecordBuilder
   */
  public static com.gottaeat.domain.common.GeoEncodedAddress.Builder newBuilder() {
    return new com.gottaeat.domain.common.GeoEncodedAddress.Builder();
  }

  /**
   * Creates a new GeoEncodedAddress RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new GeoEncodedAddress RecordBuilder
   */
  public static com.gottaeat.domain.common.GeoEncodedAddress.Builder newBuilder(com.gottaeat.domain.common.GeoEncodedAddress.Builder other) {
    if (other == null) {
      return new com.gottaeat.domain.common.GeoEncodedAddress.Builder();
    } else {
      return new com.gottaeat.domain.common.GeoEncodedAddress.Builder(other);
    }
  }

  /**
   * Creates a new GeoEncodedAddress RecordBuilder by copying an existing GeoEncodedAddress instance.
   * @param other The existing instance to copy.
   * @return A new GeoEncodedAddress RecordBuilder
   */
  public static com.gottaeat.domain.common.GeoEncodedAddress.Builder newBuilder(com.gottaeat.domain.common.GeoEncodedAddress other) {
    if (other == null) {
      return new com.gottaeat.domain.common.GeoEncodedAddress.Builder();
    } else {
      return new com.gottaeat.domain.common.GeoEncodedAddress.Builder(other);
    }
  }

  /**
   * RecordBuilder for GeoEncodedAddress instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<GeoEncodedAddress>
    implements org.apache.avro.data.RecordBuilder<GeoEncodedAddress> {

    private com.gottaeat.domain.common.Address address;
    private com.gottaeat.domain.common.Address.Builder addressBuilder;
    private com.gottaeat.domain.common.LatLon geo;
    private com.gottaeat.domain.common.LatLon.Builder geoBuilder;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gottaeat.domain.common.GeoEncodedAddress.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.address)) {
        this.address = data().deepCopy(fields()[0].schema(), other.address);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (other.hasAddressBuilder()) {
        this.addressBuilder = com.gottaeat.domain.common.Address.newBuilder(other.getAddressBuilder());
      }
      if (isValidValue(fields()[1], other.geo)) {
        this.geo = data().deepCopy(fields()[1].schema(), other.geo);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (other.hasGeoBuilder()) {
        this.geoBuilder = com.gottaeat.domain.common.LatLon.newBuilder(other.getGeoBuilder());
      }
    }

    /**
     * Creates a Builder by copying an existing GeoEncodedAddress instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gottaeat.domain.common.GeoEncodedAddress other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.address)) {
        this.address = data().deepCopy(fields()[0].schema(), other.address);
        fieldSetFlags()[0] = true;
      }
      this.addressBuilder = null;
      if (isValidValue(fields()[1], other.geo)) {
        this.geo = data().deepCopy(fields()[1].schema(), other.geo);
        fieldSetFlags()[1] = true;
      }
      this.geoBuilder = null;
    }

    /**
      * Gets the value of the 'address' field.
      * @return The value.
      */
    public com.gottaeat.domain.common.Address getAddress() {
      return address;
    }


    /**
      * Sets the value of the 'address' field.
      * @param value The value of 'address'.
      * @return This builder.
      */
    public com.gottaeat.domain.common.GeoEncodedAddress.Builder setAddress(com.gottaeat.domain.common.Address value) {
      validate(fields()[0], value);
      this.addressBuilder = null;
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
     * Gets the Builder instance for the 'address' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public com.gottaeat.domain.common.Address.Builder getAddressBuilder() {
      if (addressBuilder == null) {
        if (hasAddress()) {
          setAddressBuilder(com.gottaeat.domain.common.Address.newBuilder(address));
        } else {
          setAddressBuilder(com.gottaeat.domain.common.Address.newBuilder());
        }
      }
      return addressBuilder;
    }

    /**
     * Sets the Builder instance for the 'address' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public com.gottaeat.domain.common.GeoEncodedAddress.Builder setAddressBuilder(com.gottaeat.domain.common.Address.Builder value) {
      clearAddress();
      addressBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'address' field has an active Builder instance
     * @return True if the 'address' field has an active Builder instance
     */
    public boolean hasAddressBuilder() {
      return addressBuilder != null;
    }

    /**
      * Clears the value of the 'address' field.
      * @return This builder.
      */
    public com.gottaeat.domain.common.GeoEncodedAddress.Builder clearAddress() {
      address = null;
      addressBuilder = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'geo' field.
      * @return The value.
      */
    public com.gottaeat.domain.common.LatLon getGeo() {
      return geo;
    }


    /**
      * Sets the value of the 'geo' field.
      * @param value The value of 'geo'.
      * @return This builder.
      */
    public com.gottaeat.domain.common.GeoEncodedAddress.Builder setGeo(com.gottaeat.domain.common.LatLon value) {
      validate(fields()[1], value);
      this.geoBuilder = null;
      this.geo = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'geo' field has been set.
      * @return True if the 'geo' field has been set, false otherwise.
      */
    public boolean hasGeo() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'geo' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public com.gottaeat.domain.common.LatLon.Builder getGeoBuilder() {
      if (geoBuilder == null) {
        if (hasGeo()) {
          setGeoBuilder(com.gottaeat.domain.common.LatLon.newBuilder(geo));
        } else {
          setGeoBuilder(com.gottaeat.domain.common.LatLon.newBuilder());
        }
      }
      return geoBuilder;
    }

    /**
     * Sets the Builder instance for the 'geo' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public com.gottaeat.domain.common.GeoEncodedAddress.Builder setGeoBuilder(com.gottaeat.domain.common.LatLon.Builder value) {
      clearGeo();
      geoBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'geo' field has an active Builder instance
     * @return True if the 'geo' field has an active Builder instance
     */
    public boolean hasGeoBuilder() {
      return geoBuilder != null;
    }

    /**
      * Clears the value of the 'geo' field.
      * @return This builder.
      */
    public com.gottaeat.domain.common.GeoEncodedAddress.Builder clearGeo() {
      geo = null;
      geoBuilder = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GeoEncodedAddress build() {
      try {
        GeoEncodedAddress record = new GeoEncodedAddress();
        if (addressBuilder != null) {
          try {
            record.address = this.addressBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("address"));
            throw e;
          }
        } else {
          record.address = fieldSetFlags()[0] ? this.address : (com.gottaeat.domain.common.Address) defaultValue(fields()[0]);
        }
        if (geoBuilder != null) {
          try {
            record.geo = this.geoBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("geo"));
            throw e;
          }
        } else {
          record.geo = fieldSetFlags()[1] ? this.geo : (com.gottaeat.domain.common.LatLon) defaultValue(fields()[1]);
        }
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<GeoEncodedAddress>
    WRITER$ = (org.apache.avro.io.DatumWriter<GeoEncodedAddress>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<GeoEncodedAddress>
    READER$ = (org.apache.avro.io.DatumReader<GeoEncodedAddress>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    this.address.customEncode(out);

    if (this.geo == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      this.geo.customEncode(out);
    }

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      if (this.address == null) {
        this.address = new com.gottaeat.domain.common.Address();
      }
      this.address.customDecode(in);

      if (in.readIndex() != 1) {
        in.readNull();
        this.geo = null;
      } else {
        if (this.geo == null) {
          this.geo = new com.gottaeat.domain.common.LatLon();
        }
        this.geo.customDecode(in);
      }

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          if (this.address == null) {
            this.address = new com.gottaeat.domain.common.Address();
          }
          this.address.customDecode(in);
          break;

        case 1:
          if (in.readIndex() != 1) {
            in.readNull();
            this.geo = null;
          } else {
            if (this.geo == null) {
              this.geo = new com.gottaeat.domain.common.LatLon();
            }
            this.geo.customDecode(in);
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










