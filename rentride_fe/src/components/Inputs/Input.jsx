import React from "react";

export default function Input(props) {
  const handleChange = (e) => {
    const { type, value, files } = e.target;
    if (type === "file") {
      props.setMethod(props.multiple ? Array.from(files) : files[0]);
    } else {
      props.setMethod(value);
    }
  };

  return (
    <div className={`input-wrapper ${props.wrapperClass}`}>
      {props.type === "dropdown" ? (
        <select
          className={`${props.className} ${props.error ? "border-danger" : ""}`}
          value={props.value}
          onChange={handleChange}
        >
          <option value="">{props.placeholder}</option>
          {props.collection.map((item) => (
            <option key={item.id} value={item.id}>
              {item.name}
            </option>
          ))}
        </select>
      ) : (
        <input
          id={props.id}
          type={props.type}
          className={`${props.className} ${props.error ? "border-danger" : ""}`}
          placeholder={props.placeholder}
          value={props.type !== "file" ? props.value : undefined}
          onChange={handleChange}
          multiple={props.multiple || false}
        />
      )}
    </div>
  );
}
