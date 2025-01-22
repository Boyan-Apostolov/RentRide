import React from "react";

export default function StatisticBubbleComponent(props) {
  return (
    <div
      className={`round-edge light-gray-background p-3 text-white ${props.className}`}
    >
      <h3>{props.title}</h3>
      <div className="d-flex justify-content-center align-items-center gap-25 fs-30">
        <i className={props.icon}></i>
        <span>{props.value}</span>
      </div>
    </div>
  );
}
