/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.gottaeat.domain.order;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class PaymentMethod extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7134131209403099714L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PaymentMethod\",\"namespace\":\"com.gottaeat.domain.order\",\"fields\":[{\"name\":\"payment_method\",\"type\":[{\"type\":\"record\",\"name\":\"CreditCard\",\"namespace\":\"com.gottaeat.domain.payment\",\"fields\":[{\"name\":\"card_type\",\"type\":{\"type\":\"enum\",\"name\":\"CardType\",\"symbols\":[\"MASTERCARD\",\"AMEX\",\"VISA\",\"DISCOVER\"]}},{\"name\":\"account_number\",\"type\":\"string\"},{\"name\":\"billing_zip\",\"type\":\"string\"},{\"name\":\"ccv\",\"type\":\"string\"}]},{\"type\":\"record\",\"name\":\"DebitCard\",\"namespace\":\"com.gottaeat.domain.payment\",\"fields\":[{\"name\":\"card_type\",\"type\":\"CardType\"},{\"name\":\"account_number\",\"type\":\"string\"},{\"name\":\"billing_zip\",\"type\":\"string\"},{\"name\":\"pin\",\"type\":\"string\"}]},{\"type\":\"record\",\"name\":\"ElectronicCheck\",\"namespace\":\"com.gottaeat.domain.payment\",\"fields\":[{\"name\":\"routingNumber\",\"type\":\"string\"},{\"name\":\"accountNumber\",\"type\":\"string\"}]}]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<PaymentMethod> ENCODER =
      new BinaryMessageEncoder<PaymentMethod>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<PaymentMethod> DECODER =
      new BinaryMessageDecoder<PaymentMethod>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<PaymentMethod> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<PaymentMethod> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<PaymentMethod> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<PaymentMethod>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this PaymentMethod to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a PaymentMethod from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a PaymentMethod instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static PaymentMethod fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.Object payment_method;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PaymentMethod() {}

  /**
   * All-args constructor.
   * @param payment_method The new value for payment_method
   */
  public PaymentMethod(java.lang.Object payment_method) {
    this.payment_method = payment_method;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return payment_method;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: payment_method = value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'payment_method' field.
   * @return The value of the 'payment_method' field.
   */
  public java.lang.Object getPaymentMethod() {
    return payment_method;
  }


  /**
   * Sets the value of the 'payment_method' field.
   * @param value the value to set.
   */
  public void setPaymentMethod(java.lang.Object value) {
    this.payment_method = value;
  }

  /**
   * Creates a new PaymentMethod RecordBuilder.
   * @return A new PaymentMethod RecordBuilder
   */
  public static com.gottaeat.domain.order.PaymentMethod.Builder newBuilder() {
    return new com.gottaeat.domain.order.PaymentMethod.Builder();
  }

  /**
   * Creates a new PaymentMethod RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PaymentMethod RecordBuilder
   */
  public static com.gottaeat.domain.order.PaymentMethod.Builder newBuilder(com.gottaeat.domain.order.PaymentMethod.Builder other) {
    if (other == null) {
      return new com.gottaeat.domain.order.PaymentMethod.Builder();
    } else {
      return new com.gottaeat.domain.order.PaymentMethod.Builder(other);
    }
  }

  /**
   * Creates a new PaymentMethod RecordBuilder by copying an existing PaymentMethod instance.
   * @param other The existing instance to copy.
   * @return A new PaymentMethod RecordBuilder
   */
  public static com.gottaeat.domain.order.PaymentMethod.Builder newBuilder(com.gottaeat.domain.order.PaymentMethod other) {
    if (other == null) {
      return new com.gottaeat.domain.order.PaymentMethod.Builder();
    } else {
      return new com.gottaeat.domain.order.PaymentMethod.Builder(other);
    }
  }

  /**
   * RecordBuilder for PaymentMethod instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PaymentMethod>
    implements org.apache.avro.data.RecordBuilder<PaymentMethod> {

    private java.lang.Object payment_method;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gottaeat.domain.order.PaymentMethod.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.payment_method)) {
        this.payment_method = data().deepCopy(fields()[0].schema(), other.payment_method);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing PaymentMethod instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gottaeat.domain.order.PaymentMethod other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.payment_method)) {
        this.payment_method = data().deepCopy(fields()[0].schema(), other.payment_method);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'payment_method' field.
      * @return The value.
      */
    public java.lang.Object getPaymentMethod() {
      return payment_method;
    }


    /**
      * Sets the value of the 'payment_method' field.
      * @param value The value of 'payment_method'.
      * @return This builder.
      */
    public com.gottaeat.domain.order.PaymentMethod.Builder setPaymentMethod(java.lang.Object value) {
      validate(fields()[0], value);
      this.payment_method = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'payment_method' field has been set.
      * @return True if the 'payment_method' field has been set, false otherwise.
      */
    public boolean hasPaymentMethod() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'payment_method' field.
      * @return This builder.
      */
    public com.gottaeat.domain.order.PaymentMethod.Builder clearPaymentMethod() {
      payment_method = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PaymentMethod build() {
      try {
        PaymentMethod record = new PaymentMethod();
        record.payment_method = fieldSetFlags()[0] ? this.payment_method :  defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<PaymentMethod>
    WRITER$ = (org.apache.avro.io.DatumWriter<PaymentMethod>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<PaymentMethod>
    READER$ = (org.apache.avro.io.DatumReader<PaymentMethod>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










