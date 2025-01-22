import React, { useState, useEffect } from "react";
import { CONSTANTS } from "../../consts";

function Pagination({ onPageChange, totalCount, textColor }) {
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const calculatedPages = Math.ceil(totalCount / CONSTANTS.DEFAULT_PAGE_SIZE);
    setTotalPages(calculatedPages);
  }, [totalCount, CONSTANTS.DEFAULT_PAGE_SIZE]);

  const handlePrevious = () => {
    if (currentPage > 0) {
      const newPage = currentPage - 1;
      setCurrentPage(newPage);
      onPageChange(newPage);
    }
  };

  const handleNext = () => {
    if (currentPage < totalPages - 1) {
      const newPage = currentPage + 1;
      setCurrentPage(newPage);
      onPageChange(newPage);
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center">
      <button
        className="btn btn-sm btn-white m-2"
        onClick={handlePrevious}
        disabled={currentPage <= 0}
      >
        Previous
      </button>
      <span className={`text-${textColor}`}>
        Page {currentPage + 1} / {totalPages}
      </span>
      <button
        className="btn btn-sm btn-white next-page-btn m-2"
        onClick={handleNext}
        disabled={currentPage >= totalPages - 1}
      >
        Next
      </button>
    </div>
  );
}

export default Pagination;
