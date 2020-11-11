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
public class OrderDetail extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 4801691053003993720L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"OrderDetail\",\"namespace\":\"com.gottaeat.domain.order\",\"fields\":[{\"name\":\"quantity\",\"type\":\"int\"},{\"name\":\"total\",\"type\":\"float\"},{\"name\":\"food_item\",\"type\":{\"type\":\"record\",\"name\":\"MenuItem\",\"namespace\":\"com.gottaeat.domain.resturant\",\"fields\":[{\"name\":\"item_id\",\"type\":\"long\"},{\"name\":\"item_name\",\"type\":\"string\"},{\"name\":\"item_description\",\"type\":\"string\"},{\"name\":\"customizations\",\"type\":{\"type\":\"array\",\"items\":\"string\"},\"default\":[\"\"]},{\"name\":\"price\",\"type\":\"float\"}]}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<OrderDetail> ENCODER =
      new BinaryMessageEncoder<OrderDetail>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<OrderDetail> DECODER =
      new BinaryMessageDecoder<OrderDetail>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<OrderDetail> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<OrderDetail> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<OrderDetail> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<OrderDetail>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this OrderDetail to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a OrderDetail from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a OrderDetail instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static OrderDetail fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public int quantity;
  @Deprecated public float total;
  @Deprecated public com.gottaeat.domain.resturant.MenuItem food_item;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public OrderDetail() {}

  /**
   * All-args constructor.
   * @param quantity The new value for quantity
   * @param total The new value for total
   * @param food_item The new value for food_item
   */
  public OrderDetail(java.lang.Integer quantity, java.lang.Float total, com.gottaeat.domain.resturant.MenuItem food_item) {
    this.quantity = quantity;
    this.total = total;
    this.food_item = food_item;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return quantity;
    case 1: return total;
    case 2: return food_item;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: quantity = (java.lang.Integer)value$; break;
    case 1: total = (java.lang.Float)value$; break;
    case 2: food_item = (com.gottaeat.domain.resturant.MenuItem)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'quantity' field.
   * @return The value of the 'quantity' field.
   */
  public int getQuantity() {
    return quantity;
  }


  /**
   * Sets the value of the 'quantity' field.
   * @param value the value to set.
   */
  public void setQuantity(int value) {
    this.quantity = value;
  }

  /**
   * Gets the value of the 'total' field.
   * @return The value of the 'total' field.
   */
  public float getTotal() {
    return total;
  }


  /**
   * Sets the value of the 'total' field.
   * @param value the value to set.
   */
  public void setTotal(float value) {
    this.total = value;
  }

  /**
   * Gets the value of the 'food_item' field.
   * @return The value of the 'food_item' field.
   */
  public com.gottaeat.domain.resturant.MenuItem getFoodItem() {
    return food_item;
  }


  /**
   * Sets the value of the 'food_item' field.
   * @param value the value to set.
   */
  public void setFoodItem(com.gottaeat.domain.resturant.MenuItem value) {
    this.food_item = value;
  }

  /**
   * Creates a new OrderDetail RecordBuilder.
   * @return A new OrderDetail RecordBuilder
   */
  public static com.gottaeat.domain.order.OrderDetail.Builder newBuilder() {
    return new com.gottaeat.domain.order.OrderDetail.Builder();
  }

  /**
   * Creates a new OrderDetail RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new OrderDetail RecordBuilder
   */
  public static com.gottaeat.domain.order.OrderDetail.Builder newBuilder(com.gottaeat.domain.order.OrderDetail.Builder other) {
    if (other == null) {
      return new com.gottaeat.domain.order.OrderDetail.Builder();
    } else {
      return new com.gottaeat.domain.order.OrderDetail.Builder(other);
    }
  }

  /**
   * Creates a new OrderDetail RecordBuilder by copying an existing OrderDetail instance.
   * @param other The existing instance to copy.
   * @return A new OrderDetail RecordBuilder
   */
  public static com.gottaeat.domain.order.OrderDetail.Builder newBuilder(com.gottaeat.domain.order.OrderDetail other) {
    if (other == null) {
      return new com.gottaeat.domain.order.OrderDetail.Builder();
    } else {
      return new com.gottaeat.domain.order.OrderDetail.Builder(other);
    }
  }

  /**
   * RecordBuilder for OrderDetail instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<OrderDetail>
    implements org.apache.avro.data.RecordBuilder<OrderDetail> {

    private int quantity;
    private float total;
    private com.gottaeat.domain.resturant.MenuItem food_item;
    private com.gottaeat.domain.resturant.MenuItem.Builder food_itemBuilder;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gottaeat.domain.order.OrderDetail.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.quantity)) {
        this.quantity = data().deepCopy(fields()[0].schema(), other.quantity);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.total)) {
        this.total = data().deepCopy(fields()[1].schema(), other.total);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.food_item)) {
        this.food_item = data().deepCopy(fields()[2].schema(), other.food_item);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (other.hasFoodItemBuilder()) {
        this.food_itemBuilder = com.gottaeat.domain.resturant.MenuItem.newBuilder(other.getFoodItemBuilder());
      }
    }

    /**
     * Creates a Builder by copying an existing OrderDetail instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gottaeat.domain.order.OrderDetail other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.quantity)) {
        this.quantity = data().deepCopy(fields()[0].schema(), other.quantity);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.total)) {
        this.total = data().deepCopy(fields()[1].schema(), other.total);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.food_item)) {
        this.food_item = data().deepCopy(fields()[2].schema(), other.food_item);
        fieldSetFlags()[2] = true;
      }
      this.food_itemBuilder = null;
    }

    /**
      * Gets the value of the 'quantity' field.
      * @return The value.
      */
    public int getQuantity() {
      return quantity;
    }


    /**
      * Sets the value of the 'quantity' field.
      * @param value The value of 'quantity'.
      * @return This builder.
      */
    public com.gottaeat.domain.order.OrderDetail.Builder setQuantity(int value) {
      validate(fields()[0], value);
      this.quantity = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'quantity' field has been set.
      * @return True if the 'quantity' field has been set, false otherwise.
      */
    public boolean hasQuantity() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'quantity' field.
      * @return This builder.
      */
    public com.gottaeat.domain.order.OrderDetail.Builder clearQuantity() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'total' field.
      * @return The value.
      */
    public float getTotal() {
      return total;
    }


    /**
      * Sets the value of the 'total' field.
      * @param value The value of 'total'.
      * @return This builder.
      */
    public com.gottaeat.domain.order.OrderDetail.Builder setTotal(float value) {
      validate(fields()[1], value);
      this.total = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'total' field has been set.
      * @return True if the 'total' field has been set, false otherwise.
      */
    public boolean hasTotal() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'total' field.
      * @return This builder.
      */
    public com.gottaeat.domain.order.OrderDetail.Builder clearTotal() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'food_item' field.
      * @return The value.
      */
    public com.gottaeat.domain.resturant.MenuItem getFoodItem() {
      return food_item;
    }


    /**
      * Sets the value of the 'food_item' field.
      * @param value The value of 'food_item'.
      * @return This builder.
      */
    public com.gottaeat.domain.order.OrderDetail.Builder setFoodItem(com.gottaeat.domain.resturant.MenuItem value) {
      validate(fields()[2], value);
      this.food_itemBuilder = null;
      this.food_item = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'food_item' field has been set.
      * @return True if the 'food_item' field has been set, false otherwise.
      */
    public boolean hasFoodItem() {
      return fieldSetFlags()[2];
    }

    /**
     * Gets the Builder instance for the 'food_item' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public com.gottaeat.domain.resturant.MenuItem.Builder getFoodItemBuilder() {
      if (food_itemBuilder == null) {
        if (hasFoodItem()) {
          setFoodItemBuilder(com.gottaeat.domain.resturant.MenuItem.newBuilder(food_item));
        } else {
          setFoodItemBuilder(com.gottaeat.domain.resturant.MenuItem.newBuilder());
        }
      }
      return food_itemBuilder;
    }

    /**
     * Sets the Builder instance for the 'food_item' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public com.gottaeat.domain.order.OrderDetail.Builder setFoodItemBuilder(com.gottaeat.domain.resturant.MenuItem.Builder value) {
      clearFoodItem();
      food_itemBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'food_item' field has an active Builder instance
     * @return True if the 'food_item' field has an active Builder instance
     */
    public boolean hasFoodItemBuilder() {
      return food_itemBuilder != null;
    }

    /**
      * Clears the value of the 'food_item' field.
      * @return This builder.
      */
    public com.gottaeat.domain.order.OrderDetail.Builder clearFoodItem() {
      food_item = null;
      food_itemBuilder = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OrderDetail build() {
      try {
        OrderDetail record = new OrderDetail();
        record.quantity = fieldSetFlags()[0] ? this.quantity : (java.lang.Integer) defaultValue(fields()[0]);
        record.total = fieldSetFlags()[1] ? this.total : (java.lang.Float) defaultValue(fields()[1]);
        if (food_itemBuilder != null) {
          try {
            record.food_item = this.food_itemBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("food_item"));
            throw e;
          }
        } else {
          record.food_item = fieldSetFlags()[2] ? this.food_item : (com.gottaeat.domain.resturant.MenuItem) defaultValue(fields()[2]);
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
  private static final org.apache.avro.io.DatumWriter<OrderDetail>
    WRITER$ = (org.apache.avro.io.DatumWriter<OrderDetail>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<OrderDetail>
    READER$ = (org.apache.avro.io.DatumReader<OrderDetail>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeInt(this.quantity);

    out.writeFloat(this.total);

    this.food_item.customEncode(out);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.quantity = in.readInt();

      this.total = in.readFloat();

      if (this.food_item == null) {
        this.food_item = new com.gottaeat.domain.resturant.MenuItem();
      }
      this.food_item.customDecode(in);

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.quantity = in.readInt();
          break;

        case 1:
          this.total = in.readFloat();
          break;

        case 2:
          if (this.food_item == null) {
            this.food_item = new com.gottaeat.domain.resturant.MenuItem();
          }
          this.food_item.customDecode(in);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










