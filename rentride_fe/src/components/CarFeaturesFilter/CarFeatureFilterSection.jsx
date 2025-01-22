import React from "react";

export default function CarFeatureFilterSection(props) {
  return (
    <div>
      <h5>{props.featureName}</h5>

      <div className={`d-flex justify-content-between f-${props.featureName}`}>
        {props.featureItems?.map((fi) => {
          return (
            <div key={fi.id}>
              <input
                type="radio"
                id={fi.id}
                name={props.featureName}
                checked={props.checkedItems[fi.id]}
                className="featureFilterInput mr-2"
                value={fi.id}
                onChange={(e) =>
                  props.changeFilterCallback(e, props.featureName)
                }
              />
              <label className="fs-20" htmlFor={fi.id}>
                {fi.featureText}
              </label>
            </div>
          );
        })}
      </div>
    </div>
  );
}
