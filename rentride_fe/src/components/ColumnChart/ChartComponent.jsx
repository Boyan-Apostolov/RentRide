import React from "react";
import { Bar, Pie, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  ArcElement,
  Title,
  Tooltip,
  Legend
);

const ChartComponent = ({ dataCollection, type, title = "-" }) => {
  const colors = [
    "rgba(255, 99, 132, 0.6)",
    "rgba(54, 162, 235, 0.6)",
    "rgba(255, 206, 86, 0.6)",
    "rgba(75, 192, 192, 0.6)",
    "rgba(153, 102, 255, 0.6)",
  ];
  let chartData = {};

  if (type == "over-time" && dataCollection.length > 0) {
    const uniqueMonths = [
      ...new Set(dataCollection.map((item) => `${item.month} ${item.year}`)),
    ];
    const uniqueCars = [...new Set(dataCollection.map((item) => item.car))];

    const datasets = uniqueCars.map((car, index) => {
      const carData = uniqueMonths.map((month) => {
        const record = dataCollection.find(
          (item) => `${item.month} ${item.year}` === month && item.car === car
        );
        return record ? record.count : 0;
      });

      return {
        label: car,
        data: carData,
        backgroundColor: colors[index % colors.length],
        borderColor: colors[index % colors.length],
        borderWidth: 1,
      };
    });

    let labels = uniqueMonths;
    chartData = {
      labels: labels,
      datasets: datasets,
    };
  } else {
    chartData = {
      labels: dataCollection.map((item) => item.key),
      datasets: [
        {
          label: "Show data",
          data: dataCollection.map((item) => item.value),
          backgroundColor: colors,
          borderColor: colors,
          borderWidth: 1,
        },
      ],
    };
  }

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: type === "pie" ? "bottom" : "top",
      },
      title: {
        display: true,
        text: title,
      },
    },
    scales:
      type === "line" || type === "column" || type === "over-time"
        ? {
            x: { title: { display: true, text: title } },
            y: { title: { display: true, text: title } },
          }
        : {},
  };

  return (
    <div className="bg-white d-flex justify-content-center">
      {type === "over-time" && <Bar data={chartData} options={options} />}

      {type === "column" && <Bar data={chartData} options={options} />}
      {type === "line" && <Line data={chartData} options={options} />}
      {type === "pie" && (
        <Pie
          style={{
            maxHeight: "300px",
          }}
          data={chartData}
          options={options}
        />
      )}
    </div>
  );
};

export default ChartComponent;
