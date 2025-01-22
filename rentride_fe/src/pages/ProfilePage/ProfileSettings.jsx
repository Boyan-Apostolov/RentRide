import React, { useState } from "react";
import * as jwtDecode from "jwt-decode";
import * as Yup from "yup";
import Input from "../../components/Inputs/Input";
import {
  updateUserData,
  updateUserGoogleOAuthData,
} from "../../api/userService";
import { getCurrentUserId } from "../../api/authService";
import { GoogleLogin } from "@react-oauth/google";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";

const today = new Date();
const eighteenYearsAgo = new Date(
  today.getFullYear() - 18,
  today.getMonth(),
  today.getDate()
);

const validationSchema = Yup.object().shape({
  firstName: Yup.string().required(),
  lastName: Yup.string().required(),
  email: Yup.string().email().required(),
  birthDate: Yup.date().max(eighteenYearsAgo).required(),
  currentPassword: Yup.string(),
  newPassword: Yup.string(),
  confirmPassword: Yup.string(),
});

export default function ProfileSettings({ userData, onDataChanged }) {
  const [formData, setFormData] = useState({
    firstName: userData.name.split(" ")[0],
    lastName: userData.name.split(" ")[1],
    email: userData.email,
    birthDate: userData.birthDate,
    changePassword: false,
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
    googleOAuthId: userData.googleOAuthId,
  });

  const [isLoading, setIsLoading] = useState(false);

  const [errors, setErrors] = useState({});

  const validateField = async (field, value) => {
    try {
      await validationSchema.validateAt(field, { ...formData, [field]: value });
      setErrors((prev) => ({ ...prev, [field]: null }));
    } catch (error) {
      setErrors((prev) => ({ ...prev, [field]: error.message }));
    }
  };

  const handleInputChange = (field, value) => {
    setFormData((prevData) => ({ ...prevData, [field]: value }));
    validateField(field, value);
  };

  const linkGoogleAccount = async (googleData) => {
    setIsLoading(true);

    await updateUserGoogleOAuthData(
      getCurrentUserId(),
      jwtDecode.jwtDecode(googleData.credential)?.sub
    );
    setIsLoading(false);
    onDataChanged();
  };
  const unLinkGoogleAccount = async () => {
    setIsLoading(true);

    await updateUserGoogleOAuthData(getCurrentUserId(), "");

    setIsLoading(false);
    onDataChanged();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await validationSchema.validate(formData, { abortEarly: false });
      setErrors({});

      if (
        formData.changePassword &&
        formData.newPassword !== formData.confirmPassword
      ) {
        setErrors({ confirmPassword: "Passwords must match" });
        return;
      }

      setIsLoading(true);
      await updateUserData(getCurrentUserId(), {
        name: `${formData.firstName} ${formData.lastName}`,
        email: formData.email,
        birthDate: formData.birthDate,
        currentPassword: formData.currentPassword,
        newPassword: formData.newPassword,
      });
      setIsLoading(false);

      onDataChanged();
    } catch (err) {
      const validationErrors = {};
      if (Array.isArray(err.inner)) {
        err.inner.forEach((error) => {
          validationErrors[error.path] = error.message;
        });
      } else if (err.path) {
        validationErrors[err.path] = err.message;
      }
      setErrors(validationErrors);
    }
  };

  return (
    <div className="w-90 m-auto light-gray-background round-edge m-3 p-3 text-white">
      <h4 className="text-center">Profile Settings</h4>
      <form onSubmit={handleSubmit}>
        <div className="row mb-3">
          <div className="col-md-6">
            <label className="form-label">First Name</label>
            <Input
              name="firstName"
              className="form-control"
              value={formData.firstName}
              setMethod={(value) => handleInputChange("firstName", value)}
              error={errors.firstName}
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Last Name</label>
            <Input
              name="lastName"
              className="form-control"
              value={formData.lastName}
              setMethod={(value) => handleInputChange("lastName", value)}
              error={errors.lastName}
            />
          </div>
        </div>
        <div className="row mb-3">
          <div className="col-md-6">
            <label className="form-label">Email Address</label>
            <Input
              name="email"
              type="email"
              className="form-control"
              value={formData.email}
              setMethod={(value) => handleInputChange("email", value)}
              error={errors.email}
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Birth Date</label>
            <Input
              name="birthDate"
              type="date"
              className="form-control"
              value={formData.birthDate}
              setMethod={(value) => handleInputChange("birthDate", value)}
              error={errors.birthDate}
            />
          </div>
        </div>
        <div className="form-check mb-3">
          <input
            type="checkbox"
            className="form-check-input"
            id="changePassword"
            checked={formData.changePassword}
            onChange={() =>
              handleInputChange("changePassword", !formData.changePassword)
            }
          />
          <label className="form-check-label" htmlFor="changePassword">
            Change password?
          </label>
        </div>
        {formData.changePassword && (
          <div className="row mb-3">
            <div className="col-md-4">
              <label className="form-label">Current password</label>
              <Input
                name="currentPassword"
                type="password"
                className="form-control"
                value={formData.currentPassword}
                setMethod={(value) =>
                  handleInputChange("currentPassword", value)
                }
                error={errors.currentPassword}
              />
            </div>

            <div className="col-md-4">
              <label className="form-label">New password</label>
              <Input
                name="newPassword"
                type="password"
                className="form-control"
                value={formData.newPassword}
                setMethod={(value) => handleInputChange("newPassword", value)}
                error={errors.newPassword}
              />
            </div>
            <div className="col-md-4">
              <label className="form-label">Confirm new password</label>
              <Input
                name="confirmPassword"
                type="password"
                className={`form-control ${
                  errors.confirmPassword ? "is-invalid" : ""
                }`}
                value={formData.confirmPassword}
                setMethod={(value) =>
                  handleInputChange("confirmPassword", value)
                }
                error={errors.confirmPassword}
              />
              {errors.confirmPassword && (
                <div className="invalid-feedback">{errors.confirmPassword}</div>
              )}
            </div>
          </div>
        )}
        <hr></hr>
        {isLoading ? (
          <LoadingSpinner />
        ) : (
          <div>
            <div className="d-flex justify-content-between">
              <label className="form-label">Google Login:</label>
              {userData.googleOAuthId ? (
                <button
                  onClick={unLinkGoogleAccount}
                  className="btn btn-danger"
                >
                  Unlink Google Account
                </button>
              ) : (
                <GoogleLogin
                  onSuccess={linkGoogleAccount}
                  onError={() => {
                    showError("Login with Google failed!");
                  }}
                />
              )}
            </div>
            <hr></hr>

            <div className="text-center">
              <button type="submit" className="btn btn-primary">
                Save Settings
              </button>
            </div>
          </div>
        )}
      </form>
    </div>
  );
}
