import React, { useState, useEffect } from "react";
import { getAllPagedCars, getAllCarsCount } from "../../api/carService";
import NavBar from "../../components/NavBar/NavBar";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import { deleteCar } from "../../api/carService";

import { useNavigate } from "react-router-dom";
import Pagination from "../../components/Pagination/Pagination";

export default function AllCars() {
  const [cars, setCars] = useState([]);
  const [isLoadingCars, setLoadingCars] = useState(true);

  const [totalCarsCount, setTotalCars] = useState(0);

  const navigate = useNavigate();

  useEffect(() => {
    fetchAllCarsCount();
    fetchCarData(0);
  }, []);

  async function fetchAllCarsCount() {
    const count = await getAllCarsCount();
    setTotalCars(count);
  }

  async function fetchCarData(page) {
    setLoadingCars(true);
    const carData = await getAllPagedCars(page);
    setLoadingCars(false);
    setCars(carData);
  }

  async function handleCarDelete(e) {
    let confirmation = confirm("Are you sure you want to delete this car?");
    if (!confirmation) return;

    const carId = e.target.dataset.carid;
    try {
      await deleteCar(parseInt(carId));
      fetchCarData();
    } catch (error) {
      console.error(`Failed to delete car with id ${carId}:`, error);
    }
  }

  function handleCarUpdate(e) {
    const carId = e.target.dataset.carid;
    navigate(`/manage-car?carId=${carId}`);
  }

  function handleCarOpen(e) {
    const carId = e.target.dataset.carid;
    navigate(`/car-info?carId=${carId}`);
  }

  return (
    <>
      <NavBar bg="light" />
      <div className="d-flex flex-column align-items-center">
        <div className="w-90 round-edge m-3 p-3 light-gray-background text-white ">
          <h2 className="text-center">All cars</h2>

          {isLoadingCars ? (
            <LoadingSpinner />
          ) : (
            <div>
              <table className="table table-striped table-light round-edge">
                <thead>
                  <tr>
                    <th scope="col">#</th>
                    <th scope="col">Photo</th>
                    <th scope="col">Make</th>
                    <th scope="col">Model</th>
                    <th scope="col">Registration Number</th>
                    <th scope="col">Fuel consumption</th>
                    <th scope="col">Current city</th>
                    <th scope="col">Is exclusive</th>
                    <th scope="col">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {cars.map((car) => {
                    return (
                      <tr key={car.id}>
                        <th scope="row">{car.id}</th>
                        <td>
                          <img
                            className="round-edge w-210p h-100p ofc"
                            src={car.photosBase64[0]}
                          />
                        </td>
                        <td>{car.make}</td>
                        <td>{car.model}</td>
                        <td>{car.registrationNumber}</td>
                        <td>{car.fuelConsumption}</td>
                        <td>{car.city.name}</td>
                        <td>{car.exclusive ? "Yes" : "No"}</td>
                        <td>
                          <button
                            className="btn btn-primary"
                            onClick={handleCarOpen}
                            data-carid={car.id}
                          >
                            <i
                              data-carid={car.id}
                              className="fa-solid fa-arrow-up-right-from-square"
                            ></i>
                          </button>

                          <button
                            className="btn btn-warning"
                            onClick={handleCarUpdate}
                            data-carid={car.id}
                          >
                            <i
                              className="fa-solid fa-pen"
                              data-carid={car.id}
                            ></i>
                          </button>

                          <button
                            className="btn btn-danger"
                            onClick={handleCarDelete}
                            data-carid={car.id}
                          >
                            <i
                              className="fa-solid fa-trash"
                              data-carid={car.id}
                            ></i>
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
          {totalCarsCount > 0 && (
            <Pagination
              onPageChange={fetchCarData}
              totalCount={totalCarsCount}
            />
          )}
        </div>
      </div>
    </>
  );
}
