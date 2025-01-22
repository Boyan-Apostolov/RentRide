import React, { useState } from "react";
import NavBar from "../../components/NavBar/NavBar";
import * as Yup from "yup";
import { useNavigate } from "react-router-dom";
import Input from "../../components/Inputs/Input";
import { craeteDiscountPlan } from "../../api/discountPlanService";

export default function CreateDiscountPlanPage() {
  const navigate = useNavigate();

  const validationSchema = Yup.object().shape({
    title: Yup.string().required(),
    description: Yup.string().max(255).required(),
    remainingUses: Yup.number().positive().integer().max(200).required(),
    discountValue: Yup.number().positive().integer().max(100).required(),
    price: Yup.number().positive().required(),
  });

  const [formData, setFormData] = useState({
    title: "",
    description: "",
    remainingUses: "",
    discountValue: "",
    price: "",
  });

  const [errors, setErrors] = useState({});

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

      await craeteDiscountPlan(formData);

      setErrors({});

      navigate("/discount-plans");
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
          <h1 className="text-center">Create Discount Plan</h1>

          <form className="w-70 m-auto" onSubmit={handleSubmit}>
            <h4 className="text-center">Discount plan details</h4>

            <Input
              type="text"
              name="model"
              className="form-control mb-2"
              placeholder="Title"
              value={formData.title}
              setMethod={(value) => handleInputChange("title", value)}
              error={errors.title}
            />

            <Input
              type="text"
              name="model"
              className="form-control mb-2"
              placeholder="Description"
              value={formData.description}
              setMethod={(value) => handleInputChange("description", value)}
              error={errors.description}
            />

            <Input
              type="number"
              name="model"
              className="form-control mb-2"
              placeholder="Remaining uses"
              value={formData.remainingUses}
              setMethod={(value) => handleInputChange("remainingUses", value)}
              error={errors.remainingUses}
            />

            <Input
              type="number"
              name="model"
              className="form-control mb-2"
              placeholder="Discount percentage"
              value={formData.discountValue}
              setMethod={(value) => handleInputChange("discountValue", value)}
              error={errors.discountValue}
            />

            <Input
              type="number"
              name="model"
              className="form-control mb-2"
              placeholder="Price"
              value={formData.price}
              setMethod={(value) => handleInputChange("price", value)}
              error={errors.price}
            />

            <br />
            <div className="d-flex justify-content-center">
              <button type="submit" className="btn btn-success btn-xl">
                Create Discount Plan
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
