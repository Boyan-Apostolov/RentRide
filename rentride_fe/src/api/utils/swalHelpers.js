import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const MySwal = withReactContent(Swal);

export const showInputPopup = (title) => {
    return MySwal.fire({
        title: title,
        input: 'text',
        inputPlaceholder: "Please enter the data",
        showCancelButton: true,
        confirmButtonText: "Submit",
        inputValidator: (value) => {
            if (!value) {
                return "This field is required!";
            }
            return null;
        },
    }).then((result) => {
        if (result.isConfirmed) {
            return result.value;
        }
        return null;
    });
};

export const showCityInputPopup = () => {
    return MySwal.fire({
        title: "Enter City Name and Depot Address",
        html: `
          <input type="text" id="cityName" class="swal2-input" placeholder="Enter city name">
          <input type="text" id="depotAddress" class="swal2-input" placeholder="Enter depot address">
        `,
        showCancelButton: true,
        confirmButtonText: "Search",
        preConfirm: () => {
            const cityName = document.getElementById("cityName").value;
            const depotAddress = document.getElementById("depotAddress").value;

            if (!cityName || !depotAddress) {
                Swal.showValidationMessage("Both fields are required!");
                return null;
            }

            return {
                cityName: cityName,
                depotAddress: depotAddress,
            };
        },
    }).then((result) => {
        if (result.isConfirmed) {
            const { cityName, depotAddress } = result.value;
            return { cityName, depotAddress };
        }
        return null;
    });
};

export const showCityConfirmPopup = (cityData) => {
    return MySwal.fire({
        title: `City found - ${cityData.city}!`,
        text: `Coordinates: (${cityData.lat}, ${cityData.lon})`,
        showCancelButton: true,
        confirmButtonText: "Confirm",
    }).then((result) => {
        return result.isConfirmed;
    });
};

export const showError = (message) => {
    Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: message
    });
}
