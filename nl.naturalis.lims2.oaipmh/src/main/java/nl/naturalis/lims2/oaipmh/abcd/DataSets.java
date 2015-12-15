//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.15 at 03:59:56 PM CET 
//


package nl.naturalis.lims2.oaipmh.abcd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataSet" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DatasetGUID" type="{http://www.tdwg.org/schemas/abcd/2.06}String" minOccurs="0"/>
 *                   &lt;element name="TechnicalContacts" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="TechnicalContact" type="{http://www.tdwg.org/schemas/abcd/2.06}MicroAgentP" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ContentContacts">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ContentContact" type="{http://www.tdwg.org/schemas/abcd/2.06}MicroAgentP" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="OtherProviders" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="OtherProvider" type="{http://www.tdwg.org/schemas/abcd/2.06}String" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Metadata" type="{http://www.tdwg.org/schemas/abcd/2.06}ContentMetadata"/>
 *                   &lt;element name="Units">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Unit" type="{http://www.tdwg.org/schemas/abcd/2.06}Unit" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="DataSetExtension" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataSet"
})
@XmlRootElement(name = "DataSets")
public class DataSets {

    @XmlElement(name = "DataSet", required = true)
    protected List<DataSets.DataSet> dataSet;

    /**
     * Gets the value of the dataSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataSets.DataSet }
     * 
     * 
     */
    public List<DataSets.DataSet> getDataSet() {
        if (dataSet == null) {
            dataSet = new ArrayList<DataSets.DataSet>();
        }
        return this.dataSet;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DatasetGUID" type="{http://www.tdwg.org/schemas/abcd/2.06}String" minOccurs="0"/>
     *         &lt;element name="TechnicalContacts" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="TechnicalContact" type="{http://www.tdwg.org/schemas/abcd/2.06}MicroAgentP" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ContentContacts">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ContentContact" type="{http://www.tdwg.org/schemas/abcd/2.06}MicroAgentP" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="OtherProviders" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="OtherProvider" type="{http://www.tdwg.org/schemas/abcd/2.06}String" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Metadata" type="{http://www.tdwg.org/schemas/abcd/2.06}ContentMetadata"/>
     *         &lt;element name="Units">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Unit" type="{http://www.tdwg.org/schemas/abcd/2.06}Unit" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="DataSetExtension" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "datasetGUID",
        "technicalContacts",
        "contentContacts",
        "otherProviders",
        "metadata",
        "units",
        "dataSetExtension"
    })
    public static class DataSet {

        @XmlElement(name = "DatasetGUID")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String datasetGUID;
        @XmlElement(name = "TechnicalContacts")
        protected DataSets.DataSet.TechnicalContacts technicalContacts;
        @XmlElement(name = "ContentContacts", required = true)
        protected DataSets.DataSet.ContentContacts contentContacts;
        @XmlElement(name = "OtherProviders")
        protected DataSets.DataSet.OtherProviders otherProviders;
        @XmlElement(name = "Metadata", required = true)
        protected ContentMetadata metadata;
        @XmlElement(name = "Units", required = true)
        protected DataSets.DataSet.Units units;
        @XmlElement(name = "DataSetExtension")
        protected Object dataSetExtension;

        /**
         * Gets the value of the datasetGUID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatasetGUID() {
            return datasetGUID;
        }

        /**
         * Sets the value of the datasetGUID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatasetGUID(String value) {
            this.datasetGUID = value;
        }

        /**
         * Gets the value of the technicalContacts property.
         * 
         * @return
         *     possible object is
         *     {@link DataSets.DataSet.TechnicalContacts }
         *     
         */
        public DataSets.DataSet.TechnicalContacts getTechnicalContacts() {
            return technicalContacts;
        }

        /**
         * Sets the value of the technicalContacts property.
         * 
         * @param value
         *     allowed object is
         *     {@link DataSets.DataSet.TechnicalContacts }
         *     
         */
        public void setTechnicalContacts(DataSets.DataSet.TechnicalContacts value) {
            this.technicalContacts = value;
        }

        /**
         * Gets the value of the contentContacts property.
         * 
         * @return
         *     possible object is
         *     {@link DataSets.DataSet.ContentContacts }
         *     
         */
        public DataSets.DataSet.ContentContacts getContentContacts() {
            return contentContacts;
        }

        /**
         * Sets the value of the contentContacts property.
         * 
         * @param value
         *     allowed object is
         *     {@link DataSets.DataSet.ContentContacts }
         *     
         */
        public void setContentContacts(DataSets.DataSet.ContentContacts value) {
            this.contentContacts = value;
        }

        /**
         * Gets the value of the otherProviders property.
         * 
         * @return
         *     possible object is
         *     {@link DataSets.DataSet.OtherProviders }
         *     
         */
        public DataSets.DataSet.OtherProviders getOtherProviders() {
            return otherProviders;
        }

        /**
         * Sets the value of the otherProviders property.
         * 
         * @param value
         *     allowed object is
         *     {@link DataSets.DataSet.OtherProviders }
         *     
         */
        public void setOtherProviders(DataSets.DataSet.OtherProviders value) {
            this.otherProviders = value;
        }

        /**
         * Gets the value of the metadata property.
         * 
         * @return
         *     possible object is
         *     {@link ContentMetadata }
         *     
         */
        public ContentMetadata getMetadata() {
            return metadata;
        }

        /**
         * Sets the value of the metadata property.
         * 
         * @param value
         *     allowed object is
         *     {@link ContentMetadata }
         *     
         */
        public void setMetadata(ContentMetadata value) {
            this.metadata = value;
        }

        /**
         * Gets the value of the units property.
         * 
         * @return
         *     possible object is
         *     {@link DataSets.DataSet.Units }
         *     
         */
        public DataSets.DataSet.Units getUnits() {
            return units;
        }

        /**
         * Sets the value of the units property.
         * 
         * @param value
         *     allowed object is
         *     {@link DataSets.DataSet.Units }
         *     
         */
        public void setUnits(DataSets.DataSet.Units value) {
            this.units = value;
        }

        /**
         * Gets the value of the dataSetExtension property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getDataSetExtension() {
            return dataSetExtension;
        }

        /**
         * Sets the value of the dataSetExtension property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setDataSetExtension(Object value) {
            this.dataSetExtension = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="ContentContact" type="{http://www.tdwg.org/schemas/abcd/2.06}MicroAgentP" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "contentContact"
        })
        public static class ContentContacts {

            @XmlElement(name = "ContentContact", required = true)
            protected List<MicroAgentP> contentContact;

            /**
             * Gets the value of the contentContact property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the contentContact property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getContentContact().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link MicroAgentP }
             * 
             * 
             */
            public List<MicroAgentP> getContentContact() {
                if (contentContact == null) {
                    contentContact = new ArrayList<MicroAgentP>();
                }
                return this.contentContact;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="OtherProvider" type="{http://www.tdwg.org/schemas/abcd/2.06}String" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "otherProvider"
        })
        public static class OtherProviders {

            @XmlElement(name = "OtherProvider", required = true)
            @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
            @XmlSchemaType(name = "normalizedString")
            protected List<String> otherProvider;

            /**
             * Gets the value of the otherProvider property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the otherProvider property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getOtherProvider().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            public List<String> getOtherProvider() {
                if (otherProvider == null) {
                    otherProvider = new ArrayList<String>();
                }
                return this.otherProvider;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="TechnicalContact" type="{http://www.tdwg.org/schemas/abcd/2.06}MicroAgentP" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "technicalContact"
        })
        public static class TechnicalContacts {

            @XmlElement(name = "TechnicalContact", required = true)
            protected List<MicroAgentP> technicalContact;

            /**
             * Gets the value of the technicalContact property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the technicalContact property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTechnicalContact().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link MicroAgentP }
             * 
             * 
             */
            public List<MicroAgentP> getTechnicalContact() {
                if (technicalContact == null) {
                    technicalContact = new ArrayList<MicroAgentP>();
                }
                return this.technicalContact;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Unit" type="{http://www.tdwg.org/schemas/abcd/2.06}Unit" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "unit"
        })
        public static class Units {

            @XmlElement(name = "Unit", required = true)
            protected List<Unit> unit;

            /**
             * Gets the value of the unit property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the unit property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getUnit().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Unit }
             * 
             * 
             */
            public List<Unit> getUnit() {
                if (unit == null) {
                    unit = new ArrayList<Unit>();
                }
                return this.unit;
            }

        }

    }

}
