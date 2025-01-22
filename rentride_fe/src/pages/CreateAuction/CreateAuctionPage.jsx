import React, { useState, useEffect } from "react";
import NavBar from "../../components/NavBar/NavBar";
import * as Yup from "yup";
import { useNavigate } from "react-router-dom";
import Input from "../../components/Inputs/Input";
import { createAuction } from "../../api/auctionsService";
import { getExclusiveCars } from "../../api/carService";

export default function CreateAuctionPage() {
  const navigate = useNavigate();

  const validationSchema = Yup.object().shape({
    description: Yup.string().max(255).required(),
    minBidAmount: Yup.number().positive().required(),
    endDateTime: Yup.date().min(new Date()).required(),
    car: Yup.string().required(),
  });

  const [formData, setFormData] = useState({
    description: "",
    minBidAmount: "",
    endDateTime: "",
    car: "",
  });

  const [errors, setErrors] = useState({});
  const [cars, setCars] = useState([]);

  useEffect(() => {
    async function fetchCars() {
      const availableCars = await getExclusiveCars();
      setCars(availableCars);
    }
    fetchCars();
  }, []);

  const validateField = async (field, value) => {
    try {
      await validationSchema.validateAt(field, { ...formData, [field]: value });
      setErrors((prev) => ({ ...prev, [field]: false }));
    } catch (error) {
      setErrors((prev) => ({ ...prev, [field]: true }));
    }
  };

  const handleInputChange = (field, value) => {
    setFormData((prevData) => ({ ...prevData, [field]: value }));
    validateField(field, value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await validationSchema.validate(formData, { abortEarly: false });

      await createAuction(formData);

      setErrors({});
      navigate("/all-auctions");
    } catch (err) {
      const validationErrors = {};
      err.inner.forEach((error) => {
        validationErrors[error.path] = true;
      });
      setErrors(validationErrors);
    }
  };

  return (
    <>
      <NavBar bg="light" />
      <div className="d-flex flex-column align-items-center col-6 m-auto">
        <div className="w-90 round-edge m-3 p-3 light-gray-background text-white">
          <h1 className="text-center">Create Auction</h1>

          <form className="w-70 m-auto" onSubmit={handleSubmit}>
            <h4 className="text-center">Auction Details</h4>
            <div className="form-group mb-2">
              <label htmlFor="description" className="form-label text-white">
                Description
              </label>
              <Input
                id="description"
                type="text"
                className="form-control mb-2"
                placeholder="Description"
                value={formData.description}
                setMethod={(value) => handleInputChange("description", value)}
                error={errors.description}
              />
            </div>

            <div className="form-group mb-2">
              <label htmlFor="minBidAmount" className="form-label text-white">
                Mininmum bid amount
              </label>
              <Input
                id="minBidAmount"
                type="number"
                className="form-control mb-2"
                placeholder="Minimum Bid Amount (€)"
                value={formData.minBidAmount}
                setMethod={(value) => handleInputChange("minBidAmount", value)}
                error={errors.minBidAmount}
              />
            </div>

            <div className="form-group mb-2">
              <label htmlFor="endDate" className="form-label text-white">
                End Date and Time
              </label>
              <Input
                id="endDate"
                type="datetime-local"
                className="form-control mb-2"
                placeholder="End Date and Time"
                value={formData.endDateTime}
                setMethod={(value) => handleInputChange("endDateTime", value)}
                error={errors.endDateTime}
              />
            </div>

            <div className="form-group mb-2">
              <label htmlFor="car" className="form-label text-white">
                Select a Car
              </label>
              <select
                id="car"
                className={`form-control ${errors.car ? "is-invalid" : ""}`}
                value={formData.car}
                onChange={(e) => handleInputChange("car", e.target.value)}
              >
                <option value="">-- Select a Car --</option>
                {cars.map((car) => (
                  <option key={car.id} value={car.id}>
                    {car.make} {car.model} ({car.registrationNumber})
                  </option>
                ))}
              </select>
              {errors.car && (
                <div className="invalid-feedback">Car is required</div>
              )}
            </div>

            <br />
            <div className="d-flex justify-content-center">
              <button type="submit" className="btn btn-success btn-xl">
                Create Auction
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
