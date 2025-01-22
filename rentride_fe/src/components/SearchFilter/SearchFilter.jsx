import React, { useState, useEffect } from "react";
import RedirectBtn from "../Buttons/RedirectBtn";
import styles from "./SearchFilter.module.css";
import Input from "../Inputs/Input";
import * as Yup from "yup";
import { getCities } from "../../api/cityService";
import { useSearchParams, useNavigate } from "react-router-dom";
import { showError } from "../../api/utils/swalHelpers";

export default function SearchFilter(props) {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const [cities, setCities] = useState([]);
  const [formData, setFormData] = useState({
    fromCity: searchParams.get("fromCity") || "",
    toCity: searchParams.get("toCity") || "",
    fromDate: searchParams.get("fromDate") || "",
    toDate: searchParams.get("toDate") || "",
    fromTime: searchParams.get("fromTime") || "",
    toTime: searchParams.get("toTime") || "",
  });

  const [errors, setErrors] = useState({});
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  const validationSchema = Yup.object().shape({
    fromCity: Yup.string().required(),
    toCity: Yup.string().required(),
    fromDate: Yup.date().min(new Date()).required(),
    toDate: Yup.date().min(Yup.ref("fromDate")).required(),
    fromTime: Yup.string().required(),
    toTime: Yup.string().required(),
  });

  useEffect(() => {
    async function fetchCities() {
      const cityData = await getCities();
      setCities(cityData);
    }
    fetchCities();
  }, []);

  const validateField = async (field, value) => {
    try {
      await validationSchema.validateAt(field, { ...formData, [field]: value });
      setErrors((prev) => ({ ...prev, [field]: false }));
    } catch (error) {
      setErrors((prev) => ({ ...prev, [field]: error.message }));
    }
  };

  const handleInputChange = (field, value) => {
    setFormData((prevData) => ({ ...prevData, [field]: value }));
    validateField(field, value);

    validationSchema
      .isValid({ ...formData, [field]: value })
      .then((isValid) => setIsFormInvalid(!isValid));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await validationSchema.validate(formData, { abortEarly: false });

      if (formData.fromCity == formData.toCity) {
        showError("Pick-up and drop-off cities must be different.");
        return;
      }

      setErrors({});

      if (props.isForAuctionClaim) {
        props.overrideSearchFunction(formData);
        return;
      }

      const queryParams = new URLSearchParams(formData).toString();
      navigate(`/search?${queryParams}`);
    } catch (err) {
      const validationErrors = {};
      err.inner.forEach((error) => {
        validationErrors[error.path] = error.message;
      });
      setErrors(validationErrors);
    }
  };

  return (
    <div className={`${styles.searchForm} round-edge text-white text-bold`}>
      <form onSubmit={handleSubmit}>
        <p>Pick up</p>
        <Input
          type="dropdown"
          className="form-control mb-2 pickup-city"
          collection={cities}
          value={formData.fromCity}
          setMethod={(value) => handleInputChange("fromCity", value)}
          error={errors.fromCity}
          placeholder="Select a pick-up city"
        />

        <div className="d-flex gap-10">
          <Input
            type="date"
            className="form-control mb-2 pickup-date"
            placeholder="Pick-up date"
            value={formData.fromDate}
            setMethod={(value) => handleInputChange("fromDate", value)}
            error={errors.fromDate}
          />
          <Input
            type="time"
            className="form-control mb-2 pickup-time"
            placeholder="Pick-up time"
            value={formData.fromTime}
            setMethod={(value) => handleInputChange("fromTime", value)}
            error={errors.fromTime}
          />
        </div>

        <hr className="white-hr" />

        <p>Drop off</p>
        <Input
          type="dropdown"
          className="form-control mb-2 dropoff-city"
          collection={cities}
          value={formData.toCity}
          setMethod={(value) => handleInputChange("toCity", value)}
          error={errors.toCity}
          placeholder="Select a drop-off city"
        />

        <div className="d-flex gap-10">
          <Input
            type="date"
            className="form-control mb-2 dropoff-date"
            placeholder="Drop-off date"
            value={formData.toDate}
            setMethod={(value) => handleInputChange("toDate", value)}
            error={errors.toDate}
          />
          <Input
            type="time"
            className="form-control mb-2 dropoff-time"
            placeholder="Drop-off time"
            value={formData.toTime}
            setMethod={(value) => handleInputChange("toTime", value)}
            error={errors.toTime}
          />
        </div>

        <br />

        <div className="d-flex justify-content-center">
          <button
            type="submit"
            className="btn btn-success btn-xl"
            disabled={isFormInvalid}
          >
            {props.isForAuctionClaim ? "Claim" : "Search"}
          </button>
        </div>
      </form>
    </div>
  );
}
