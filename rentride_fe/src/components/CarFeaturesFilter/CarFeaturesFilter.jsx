import style from "./CarFeaturesFilter.module.css";
import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { getCarFilters } from "../../api/carService";
import CarFeatureFilterSection from "./CarFeatureFilterSection";

export default function CarFeaturesFilter(props) {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filters, setFilters] = useState([]);
  const [checkedItems, setCheckedItems] = useState({});

  async function fetchFilters() {
    const filters = await getCarFilters();
    setFilters(filters);
  }

  useEffect(() => {
    fetchFilters();
  }, []);

  useEffect(() => {
    const params = Object.fromEntries([...searchParams]);

    if (params.filters) {
      const selectedFilters = params.filters.split(",");
      const newCheckedItems = {};
      selectedFilters.forEach((filterId) => {
        newCheckedItems[filterId] = true;
      });
      setCheckedItems(newCheckedItems);
    }
  }, [searchParams]);

  const handleInputChange = (event) => {
    const { value, checked, name } = event.target;

    let updatedCheckedItems = {
      ...checkedItems,
      [value]: checked,
    };

    const currentSectionValues = [
      ...event.target.closest(`.f-${name}`).querySelectorAll("input"),
    ].map(
      (input) =>
        (updatedCheckedItems = {
          ...updatedCheckedItems,
          [+input.value]: false,
        })
    );

    updatedCheckedItems = {
      ...updatedCheckedItems,
      [value]: checked,
    };

    setCheckedItems(updatedCheckedItems);
    updateSearchParamsAndRedirect(updatedCheckedItems);
  };

  const updateSearchParamsAndRedirect = (updatedCheckedItems) => {
    const filters = Object.keys(updatedCheckedItems)
      .filter((key) => updatedCheckedItems[key])
      .join(",");

    const currentParams = Object.fromEntries([...searchParams]);
    const updatedParams = {
      ...currentParams,
      filters,
    };

    setSearchParams(updatedParams);
  };

  const clearFilters = () => {
    setCheckedItems({});

    const currentParams = Object.fromEntries([...searchParams]);
    const updatedParams = { ...currentParams };
    delete updatedParams.filters;

    setSearchParams(updatedParams);
    props.callback();
  };

  return (
    <div className="round-edge light-gray-background p-3 w-70">
      <div className="d-flex justify-content-between">
        <span className="text-bold">Filters</span>
        <a className="text-primary" onClick={clearFilters}>
          Clear all filters
        </a>
      </div>
      <hr className="white-hr"></hr>

      {Object.keys(filters).map((feature) => {
        return (
          <div key={crypto.randomUUID()}>
            <CarFeatureFilterSection
              key={crypto.randomUUID()}
              featureName={feature}
              featureItems={filters[feature]}
              checkedItems={checkedItems}
              changeFilterCallback={handleInputChange}
            />
            <hr className="white-hr"></hr>
          </div>
        );
      })}
    </div>
  );
}
