import React, { useEffect, useState } from "react";
import NavBar from "../../components/NavBar/NavBar";
import {
  getRenderServiceStatus,
  turnOffRenderService,
  turnOnRenderService,
  redeployRenderService,
} from "../../api/deploymentDataService";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import { showError } from "../../api/utils/swalHelpers";

export default function DeploymentsPage() {
  const [backendData, setBackendData] = useState({});
  const [frontendData, setFrontendData] = useState({});

  const [loadingState, setLoadingState] = useState({
    isLoadingBackendStatus: true,
    isLoadingFrontendStatus: true,
    isLoadingDbStatus: true,
  });

  async function startRenderService(serviceType) {
    if (serviceType == "backend" && backendData.status == "not_suspended") {
      showError("Backend service is already running");
      return;
    }

    if (serviceType == "frontend" && frontendData.status == "not_suspended") {
      showError("Frontend service is already running");
      return;
    }

    await turnOnRenderService(serviceType);
    updateStates(serviceType);
  }

  async function updateStates(serviceType) {
    if (serviceType == "backend") {
      fetchBackendStatus();
    } else {
      fetchFrontendStatus();
    }
  }
  async function reuploadRenderService(serviceType) {
    await redeployRenderService(serviceType);

    updateStates(serviceType);
  }

  async function stopRenderService(serviceType) {
    if (serviceType == "backend" && backendData.status == "suspended") {
      showError("Backend service is already suspended");
      return;
    }

    if (serviceType == "frontend" && frontendData.status == "suspended") {
      showError("Frontend service is already suspended");
      return;
    }

    await turnOffRenderService(serviceType);

    updateStates(serviceType);
  }

  async function openService(serviceType) {
    if (serviceType == "backend") {
      window.open("https://rentride-be.onrender.com/cities/test", "_blank");
    } else {
      window.open("https://rentride-fe.onrender.com", "_blank");
    }
  }

  async function fetchBackendStatus() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingBackendStatus: true,
    }));

    const data = await getRenderServiceStatus("backend");
    setBackendData(data);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingBackendStatus: false,
    }));
  }

  async function fetchFrontendStatus() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingFrontendStatus: true,
    }));

    const data = await getRenderServiceStatus("frontend");
    setFrontendData(data);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingFrontendStatus: false,
    }));
  }

  useEffect(() => {
    fetchBackendStatus();
    fetchFrontendStatus();
  }, []);

  function getStatusClass(status) {
    if (status == "not_suspended" || status == "RUNNING") return "bg-success";
    return "bg-danger";
  }
  const formattedDateTime = (isoString) =>
    new Date(isoString).toISOString().replace("T", " ").split(".")[0];

  return (
    <>
      <NavBar bg="light" />
      <div className="d-flex flex-column align-items-center">
        <div className="w-90 round-edge m-3 p-3 light-gray-background text-white">
          <h1 className="text-center">Deployments</h1>

          <div className="d-flex flex-column mt-5 gap-25 justify-content-center flex-wrap w-90 m-auto">
            {loadingState.isLoadingBackendStatus ? (
              <LoadingSpinner />
            ) : (
              <div className="p-3 d-flex align-items-center justify-content-between round-edge very-light-gray-background">
                <span className="text-bold">RentRide Backend</span>
                <span
                  className={`p-2 round-edge ${getStatusClass(
                    backendData.status
                  )}`}
                >
                  {backendData.status == "not_suspended" ? "ONLINE" : "OFFLINE"}
                </span>
                <span className="text-center">
                  Last deploy: <br></br>{" "}
                  {formattedDateTime(backendData.lastUpdate)}
                </span>
                <div className="d-flex flex-wrap gap-15 fb-30">
                  <button
                    onClick={() => startRenderService("backend")}
                    className="btn btn-sm btn-success fb-35"
                    title="Start"
                  >
                    Start <i class="fa-solid fa-power-off"></i>
                  </button>

                  <button
                    onClick={() => stopRenderService("backend")}
                    className="btn btn-sm btn-danger fb-35"
                    title="Suspend"
                  >
                    Suspend <i class="fa-solid fa-power-off"></i>
                  </button>

                  <button
                    onClick={() => reuploadRenderService("backend")}
                    className="btn btn-sm btn-primary fb-35"
                    title="Re-Deploy"
                  >
                    Deploy <i class="fa-solid fa-cloud-arrow-up"></i>
                  </button>

                  <button
                    onClick={() => openService("backend")}
                    className="btn btn-sm btn-primary fb-35"
                    title="Open"
                  >
                    Open <i class="fa-solid fa-up-right-from-square"></i>
                  </button>
                </div>
              </div>
            )}

            {loadingState.isLoadingFrontendStatus ? (
              <LoadingSpinner />
            ) : (
              <div className="p-3 d-flex align-items-center justify-content-between round-edge very-light-gray-background">
                <span className="text-bold">RentRide Frontend</span>
                <span
                  className={`p-2 round-edge ${getStatusClass(
                    frontendData.status
                  )}`}
                >
                  {frontendData.status == "not_suspended"
                    ? "ONLINE"
                    : "OFFLINE"}
                </span>
                <span className="text-center">
                  Last deploy: <br></br>{" "}
                  {formattedDateTime(frontendData.lastUpdate)}
                </span>
                <div className="d-flex flex-wrap gap-15 fb-30">
                  <button
                    onClick={() => startRenderService("frontend")}
                    className="btn btn-sm btn-success fb-35"
                    title="Start"
                  >
                    Start <i class="fa-solid fa-power-off"></i>
                  </button>

                  <button
                    onClick={() => stopRenderService("frontend")}
                    className="btn btn-sm btn-danger fb-35"
                    title="Suspend"
                  >
                    Suspend <i class="fa-solid fa-power-off"></i>
                  </button>

                  <button
                    onClick={() => reuploadRenderService("frontend")}
                    className="btn btn-sm btn-primary fb-35"
                    title="Re-Deploy"
                  >
                    Deploy <i class="fa-solid fa-cloud-arrow-up"></i>
                  </button>

                  <button
                    onClick={() => openService("frontend")}
                    className="btn btn-sm btn-primary fb-35"
                    title="Open"
                  >
                    Open <i class="fa-solid fa-up-right-from-square"></i>
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
