/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.gottaeat.domain.resturant;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class SolicitationResponse extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2173700209616344257L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"SolicitationResponse\",\"namespace\":\"com.gottaeat.domain.resturant\",\"fields\":[{\"name\":\"food\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"FoodOrderDetail\",\"fields\":[{\"name\":\"quantity\",\"type\":\"int\"},{\"name\":\"food_item\",\"type\":{\"type\":\"record\",\"name\":\"MenuItem\",\"fields\":[{\"name\":\"item_id\",\"type\":\"long\"},{\"name\":\"item_name\",\"type\":\"string\"},{\"name\":\"item_description\",\"type\":\"string\"},{\"name\":\"customizations\",\"type\":{\"type\":\"array\",\"items\":\"string\"},\"default\":[\"\"]},{\"name\":\"price\",\"type\":\"float\"},{\"name\":\"taxable\",\"type\":\"boolean\"}]}}]}}},{\"name\":\"resturant\",\"type\":{\"type\":\"record\",\"name\":\"Resturant\",\"fields\":[{\"name\":\"location\",\"type\":{\"type\":\"record\",\"name\":\"Address\",\"namespace\":\"com.gottaeat.domain.common\",\"fields\":[{\"name\":\"street\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"state\",\"type\":\"string\"},{\"name\":\"zip\",\"type\":\"string\"},{\"name\":\"geo\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"LatLon\",\"fields\":[{\"name\":\"latitude\",\"type\":\"double\"},{\"name\":\"longitude\",\"type\":\"double\"}]}]}]}}]}},{\"name\":\"eta\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"notificationTopic\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();
static {
    MODEL$.addLogicalTypeConversion(new org.apache.avro.data.TimeConversions.TimestampMillisConversion());
  }

  private static final BinaryMessageEncoder<SolicitationResponse> ENCODER =
      new BinaryMessageEncoder<SolicitationResponse>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<SolicitationResponse> DECODER =
      new BinaryMessageDecoder<SolicitationResponse>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<SolicitationResponse> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<SolicitationResponse> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<SolicitationResponse> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<SolicitationResponse>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this SolicitationResponse to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a SolicitationResponse from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a SolicitationResponse instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static SolicitationResponse fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail> food;
  @Deprecated public com.gottaeat.domain.resturant.Resturant resturant;
  @Deprecated public java.time.Instant eta;
  @Deprecated public java.lang.CharSequence notificationTopic;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public SolicitationResponse() {}

  /**
   * All-args constructor.
   * @param food The new value for food
   * @param resturant The new value for resturant
   * @param eta The new value for eta
   * @param notificationTopic The new value for notificationTopic
   */
  public SolicitationResponse(java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail> food, com.gottaeat.domain.resturant.Resturant resturant, java.time.Instant eta, java.lang.CharSequence notificationTopic) {
    this.food = food;
    this.resturant = resturant;
    this.eta = eta.truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
    this.notificationTopic = notificationTopic;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return food;
    case 1: return resturant;
    case 2: return eta;
    case 3: return notificationTopic;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  private static final org.apache.avro.Conversion<?>[] conversions =
      new org.apache.avro.Conversion<?>[] {
      null,
      null,
      new org.apache.avro.data.TimeConversions.TimestampMillisConversion(),
      null,
      null
  };

  @Override
  public org.apache.avro.Conversion<?> getConversion(int field) {
    return conversions[field];
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: food = (java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail>)value$; break;
    case 1: resturant = (com.gottaeat.domain.resturant.Resturant)value$; break;
    case 2: eta = (java.time.Instant)value$; break;
    case 3: notificationTopic = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'food' field.
   * @return The value of the 'food' field.
   */
  public java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail> getFood() {
    return food;
  }


  /**
   * Sets the value of the 'food' field.
   * @param value the value to set.
   */
  public void setFood(java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail> value) {
    this.food = value;
  }

  /**
   * Gets the value of the 'resturant' field.
   * @return The value of the 'resturant' field.
   */
  public com.gottaeat.domain.resturant.Resturant getResturant() {
    return resturant;
  }


  /**
   * Sets the value of the 'resturant' field.
   * @param value the value to set.
   */
  public void setResturant(com.gottaeat.domain.resturant.Resturant value) {
    this.resturant = value;
  }

  /**
   * Gets the value of the 'eta' field.
   * @return The value of the 'eta' field.
   */
  public java.time.Instant getEta() {
    return eta;
  }


  /**
   * Sets the value of the 'eta' field.
   * @param value the value to set.
   */
  public void setEta(java.time.Instant value) {
    this.eta = value.truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
  }

  /**
   * Gets the value of the 'notificationTopic' field.
   * @return The value of the 'notificationTopic' field.
   */
  public java.lang.CharSequence getNotificationTopic() {
    return notificationTopic;
  }


  /**
   * Sets the value of the 'notificationTopic' field.
   * @param value the value to set.
   */
  public void setNotificationTopic(java.lang.CharSequence value) {
    this.notificationTopic = value;
  }

  /**
   * Creates a new SolicitationResponse RecordBuilder.
   * @return A new SolicitationResponse RecordBuilder
   */
  public static com.gottaeat.domain.resturant.SolicitationResponse.Builder newBuilder() {
    return new com.gottaeat.domain.resturant.SolicitationResponse.Builder();
  }

  /**
   * Creates a new SolicitationResponse RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new SolicitationResponse RecordBuilder
   */
  public static com.gottaeat.domain.resturant.SolicitationResponse.Builder newBuilder(com.gottaeat.domain.resturant.SolicitationResponse.Builder other) {
    if (other == null) {
      return new com.gottaeat.domain.resturant.SolicitationResponse.Builder();
    } else {
      return new com.gottaeat.domain.resturant.SolicitationResponse.Builder(other);
    }
  }

  /**
   * Creates a new SolicitationResponse RecordBuilder by copying an existing SolicitationResponse instance.
   * @param other The existing instance to copy.
   * @return A new SolicitationResponse RecordBuilder
   */
  public static com.gottaeat.domain.resturant.SolicitationResponse.Builder newBuilder(com.gottaeat.domain.resturant.SolicitationResponse other) {
    if (other == null) {
      return new com.gottaeat.domain.resturant.SolicitationResponse.Builder();
    } else {
      return new com.gottaeat.domain.resturant.SolicitationResponse.Builder(other);
    }
  }

  /**
   * RecordBuilder for SolicitationResponse instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<SolicitationResponse>
    implements org.apache.avro.data.RecordBuilder<SolicitationResponse> {

    private java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail> food;
    private com.gottaeat.domain.resturant.Resturant resturant;
    private com.gottaeat.domain.resturant.Resturant.Builder resturantBuilder;
    private java.time.Instant eta;
    private java.lang.CharSequence notificationTopic;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gottaeat.domain.resturant.SolicitationResponse.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.food)) {
        this.food = data().deepCopy(fields()[0].schema(), other.food);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.resturant)) {
        this.resturant = data().deepCopy(fields()[1].schema(), other.resturant);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (other.hasResturantBuilder()) {
        this.resturantBuilder = com.gottaeat.domain.resturant.Resturant.newBuilder(other.getResturantBuilder());
      }
      if (isValidValue(fields()[2], other.eta)) {
        this.eta = data().deepCopy(fields()[2].schema(), other.eta);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.notificationTopic)) {
        this.notificationTopic = data().deepCopy(fields()[3].schema(), other.notificationTopic);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing SolicitationResponse instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gottaeat.domain.resturant.SolicitationResponse other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.food)) {
        this.food = data().deepCopy(fields()[0].schema(), other.food);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.resturant)) {
        this.resturant = data().deepCopy(fields()[1].schema(), other.resturant);
        fieldSetFlags()[1] = true;
      }
      this.resturantBuilder = null;
      if (isValidValue(fields()[2], other.eta)) {
        this.eta = data().deepCopy(fields()[2].schema(), other.eta);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.notificationTopic)) {
        this.notificationTopic = data().deepCopy(fields()[3].schema(), other.notificationTopic);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'food' field.
      * @return The value.
      */
    public java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail> getFood() {
      return food;
    }


    /**
      * Sets the value of the 'food' field.
      * @param value The value of 'food'.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder setFood(java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail> value) {
      validate(fields()[0], value);
      this.food = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'food' field has been set.
      * @return True if the 'food' field has been set, false otherwise.
      */
    public boolean hasFood() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'food' field.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder clearFood() {
      food = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'resturant' field.
      * @return The value.
      */
    public com.gottaeat.domain.resturant.Resturant getResturant() {
      return resturant;
    }


    /**
      * Sets the value of the 'resturant' field.
      * @param value The value of 'resturant'.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder setResturant(com.gottaeat.domain.resturant.Resturant value) {
      validate(fields()[1], value);
      this.resturantBuilder = null;
      this.resturant = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'resturant' field has been set.
      * @return True if the 'resturant' field has been set, false otherwise.
      */
    public boolean hasResturant() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'resturant' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public com.gottaeat.domain.resturant.Resturant.Builder getResturantBuilder() {
      if (resturantBuilder == null) {
        if (hasResturant()) {
          setResturantBuilder(com.gottaeat.domain.resturant.Resturant.newBuilder(resturant));
        } else {
          setResturantBuilder(com.gottaeat.domain.resturant.Resturant.newBuilder());
        }
      }
      return resturantBuilder;
    }

    /**
     * Sets the Builder instance for the 'resturant' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder setResturantBuilder(com.gottaeat.domain.resturant.Resturant.Builder value) {
      clearResturant();
      resturantBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'resturant' field has an active Builder instance
     * @return True if the 'resturant' field has an active Builder instance
     */
    public boolean hasResturantBuilder() {
      return resturantBuilder != null;
    }

    /**
      * Clears the value of the 'resturant' field.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder clearResturant() {
      resturant = null;
      resturantBuilder = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'eta' field.
      * @return The value.
      */
    public java.time.Instant getEta() {
      return eta;
    }


    /**
      * Sets the value of the 'eta' field.
      * @param value The value of 'eta'.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder setEta(java.time.Instant value) {
      validate(fields()[2], value);
      this.eta = value.truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'eta' field has been set.
      * @return True if the 'eta' field has been set, false otherwise.
      */
    public boolean hasEta() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'eta' field.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder clearEta() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'notificationTopic' field.
      * @return The value.
      */
    public java.lang.CharSequence getNotificationTopic() {
      return notificationTopic;
    }


    /**
      * Sets the value of the 'notificationTopic' field.
      * @param value The value of 'notificationTopic'.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder setNotificationTopic(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.notificationTopic = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'notificationTopic' field has been set.
      * @return True if the 'notificationTopic' field has been set, false otherwise.
      */
    public boolean hasNotificationTopic() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'notificationTopic' field.
      * @return This builder.
      */
    public com.gottaeat.domain.resturant.SolicitationResponse.Builder clearNotificationTopic() {
      notificationTopic = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SolicitationResponse build() {
      try {
        SolicitationResponse record = new SolicitationResponse();
        record.food = fieldSetFlags()[0] ? this.food : (java.util.List<com.gottaeat.domain.resturant.FoodOrderDetail>) defaultValue(fields()[0]);
        if (resturantBuilder != null) {
          try {
            record.resturant = this.resturantBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("resturant"));
            throw e;
          }
        } else {
          record.resturant = fieldSetFlags()[1] ? this.resturant : (com.gottaeat.domain.resturant.Resturant) defaultValue(fields()[1]);
        }
        record.eta = fieldSetFlags()[2] ? this.eta : (java.time.Instant) defaultValue(fields()[2]);
        record.notificationTopic = fieldSetFlags()[3] ? this.notificationTopic : (java.lang.CharSequence) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<SolicitationResponse>
    WRITER$ = (org.apache.avro.io.DatumWriter<SolicitationResponse>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<SolicitationResponse>
    READER$ = (org.apache.avro.io.DatumReader<SolicitationResponse>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










