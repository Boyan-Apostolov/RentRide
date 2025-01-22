import React from "react";
import { useNavigate } from "react-router-dom";

export default function RedirectBtn(props) {
  const navigate = useNavigate();

  const handleButtonClick = () => {
    if (!props.overrideStopRedirect) {
      navigate(props.redirectUrl);
      if (props.callback) props.callback();
    } else {
      alert("Cannot redirect");
    }
  };

  return (
    <div className="d-flex justify-content-center">
      <button
        onClick={handleButtonClick}
        className={`btn round-edge btn-white text-bold ${props.className}`}
      >
        {props.text}
      </button>
    </div>
  );
}
