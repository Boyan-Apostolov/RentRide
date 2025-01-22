import React, { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import * as Yup from "yup";
import style from "./ManageCarPage.module.css";
import NavBar from "../../components/NavBar/NavBar";
import { getCities } from "../../api/cityService";
import Input from "../../components/Inputs/Input";
import { getCarDetails, createCar, updateCar } from "../../api/carService";
import { fileToBase64 } from "../../api/utils/fileSaverHelper";
import { CONSTANTS } from "../../consts";

const validationSchema = Yup.object().shape({
  city: Yup.string().required(),
  make: Yup.string().required(),
  model: Yup.string().required(),
  registrationPlate: Yup.string().required(),
  fuelConsumption: Yup.number().positive().max(20).required(),
  seats: Yup.number().positive().integer().max(10).required(),
  doors: Yup.number().positive().integer().max(10).required(),
  transmission: Yup.string().required(),
  extras: Yup.string().required(),
  carPhotos: Yup.array().min(1).required(),
  isExclusive: Yup.boolean().required(),
});

export default function ManageCarPage() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  const carId = searchParams.get("carId");
  const isCreateMode = !carId;

  const [cities, setCities] = useState([]);
  const [carPhoto, setCarPhoto] = useState("");

  const [formData, setFormData] = useState({
    city: "",
    make: "",
    model: "",
    registrationPlate: "",
    fuelConsumption: "",
    seats: "",
    doors: "",
    transmission: "",
    extras: "",
    carPhotos: [],
    isExclusive: false,
  });

  const [errors, setErrors] = useState({});

  async function fetchCities() {
    const cityData = await getCities();
    setCities(cityData);
  }

  async function fetchCarData() {
    if (!isCreateMode) {
      const carData = await getCarDetails(carId);

      setFormData({
        city: carData.city.id,
        make: carData.make,
        model: carData.model,
        registrationPlate: carData.registrationNumber,
        fuelConsumption: carData.fuelConsumption,
        seats: carData.carFeatures[0].featureText,
        doors: carData.carFeatures[1].featureText,
        transmission: carData.carFeatures[2].featureText,
        extras: carData.carFeatures[3].featureText,
        carPhotos: [],
        isExclusive: carData.isExclusive || false,
      });

      setCarPhoto(carData.photosBase64[0]);
    } else {
      setCarPhoto("/src/assets/car-factory.jpg");
    }
  }

  useEffect(() => {
    fetchCities();
    fetchCarData();
  }, []);

  const transmissionType = [
    { id: 1, name: "Auto" },
    { id: 2, name: "Manual" },
  ];

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

      const photosBase64 = await Promise.all(
        formData.carPhotos.map(async (ph) => await fileToBase64(ph))
      );

      const upsertService = isCreateMode ? createCar : updateCar;
      await upsertService({
        id: carId,
        make: formData.make,
        model: formData.model,
        registrationNumber: formData.registrationPlate,
        fuelConsumption: +formData.fuelConsumption,
        cityId: +formData.city,
        photosBase64: photosBase64,
        features: [
          formData.seats,
          formData.doors,
          formData.transmission == 1 ? "Auto" : "Manual",
          formData.extras,
        ],
        isExclusive: formData.isExclusive,
      });

      setErrors({});

      navigate("/all-cars");
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
      <div className="page d-flex flex-column align-items-center">
        <div className="light-gray-background text-white round-edge w-90 m-3 p-2">
          <h2 className="text-center">
            {isCreateMode ? "Create" : "Update"} a car
          </h2>
          <div className={`d-flex text-center ${style.center}`}>
            <div>
              <form className="w-70 m-auto" onSubmit={handleSubmit}>
                <h4>Car details</h4>

                <Input
                  type="dropdown"
                  name="city"
                  className="form-control mb-2"
                  placeholder="Select a city"
                  collection={cities}
                  value={formData.city}
                  setMethod={(value) => handleInputChange("city", value)}
                  error={errors.city}
                />

                <div className="d-flex gap-25">
                  <Input
                    type="text"
                    name="make"
                    className="form-control mb-2"
                    placeholder="Make"
                    value={formData.make}
                    setMethod={(value) => handleInputChange("make", value)}
                    error={errors.make}
                  />
                  <Input
                    type="text"
                    name="model"
                    className="form-control mb-2"
                    placeholder="Model"
                    value={formData.model}
                    setMethod={(value) => handleInputChange("model", value)}
                    error={errors.model}
                  />
                </div>
                <div className="d-flex gap-25 align-items-center">
                  <Input
                    type="text"
                    name="registrationPlate"
                    className="form-control mb-2"
                    placeholder="Registration plate"
                    value={formData.registrationPlate}
                    wrapperClass="fb-90"
                    setMethod={(value) =>
                      handleInputChange("registrationPlate", value)
                    }
                    error={errors.registrationPlate}
                  />

                  <i className={CONSTANTS.featureIcons.Car}></i>
                </div>

                <div className="d-flex gap-25 align-items-center">
                  <Input
                    type="number"
                    name="fuelConsumption"
                    className="form-control mb-2"
                    placeholder="Fuel consumption per 100km (max 20.0)"
                    value={formData.fuelConsumption}
                    wrapperClass="fb-90"
                    setMethod={(value) =>
                      handleInputChange("fuelConsumption", value)
                    }
                    error={errors.fuelConsumption}
                  />

                  <i className={CONSTANTS.featureIcons.Fuel}></i>
                </div>

                <div className="d-flex gap-25 align-items-center">
                  <Input
                    type="number"
                    name="seats"
                    className="form-control mb-2"
                    placeholder="Seats"
                    value={formData.seats}
                    setMethod={(value) => handleInputChange("seats", value)}
                    error={errors.seats}
                  />
                  <i className={CONSTANTS.featureIcons.Seats}></i>
                  <Input
                    type="number"
                    name="doors"
                    className="form-control mb-2"
                    placeholder="Doors"
                    value={formData.doors}
                    setMethod={(value) => handleInputChange("doors", value)}
                    error={errors.doors}
                  />
                  <i className={CONSTANTS.featureIcons.Doors}></i>
                </div>

                <div className="d-flex gap-25 align-items-center">
                  <Input
                    type="dropdown"
                    name="transmission"
                    className="form-control mb-2"
                    placeholder="Transmission type"
                    collection={transmissionType}
                    wrapperClass="fb-90"
                    value={formData.transmission}
                    setMethod={(value) =>
                      handleInputChange("transmission", value)
                    }
                    error={errors.transmission}
                  />
                  <i className={CONSTANTS.featureIcons.Transmission}></i>
                </div>

                <div className="d-flex gap-25 align-items-center">
                  <Input
                    type="text"
                    name="seats"
                    className="form-control mb-2"
                    placeholder="Extras"
                    value={formData.extras}
                    setMethod={(value) => handleInputChange("extras", value)}
                    error={errors.extras}
                  />
                  <i className={CONSTANTS.featureIcons.Bonus}></i>

                  <Input
                    type="file"
                    name="carPhotos"
                    className="form-control mb-2"
                    multiple
                    setMethod={(files) => handleInputChange("carPhotos", files)}
                    error={errors.carPhotos}
                  />
                </div>

                <div className="d-flex gap-25 align-items-center">
                  <label className="form-check-label" htmlFor="isExclusive">
                    Exclusive Car
                  </label>
                  <input
                    type="checkbox"
                    id="isExclusive"
                    className="form-check-input"
                    checked={formData.isExclusive}
                    onChange={(e) =>
                      handleInputChange("isExclusive", e.target.checked)
                    }
                  />
                </div>

                <br />
                <div className="d-flex justify-content-center">
                  <button type="submit" className="btn btn-primary btn-xl">
                    {isCreateMode ? "Create" : "Update"} car
                  </button>
                </div>
              </form>
            </div>
            <div className="fb-50">
              <img src={carPhoto} className="round-edge" alt="Car factory" />
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
