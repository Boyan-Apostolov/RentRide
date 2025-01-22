import React from "react";

export default function AllPaymentsTable({ payments }) {
  function handlePaymentPay(stripeLink) {
    document.location.href = stripeLink;
  }

  return (
    <div className="w-90 round-edge m-3 p-3  m-auto light-gray-background text-white ">
      <h2 className="text-center">All Payments</h2>
      <table className="table table-striped table-light round-edge">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Description</th>
            <th scope="col">Date</th>
            <th scope="col">Amount</th>
            <th scope="col">User</th>
            <th scope="col">Is paid</th>
          </tr>
        </thead>
        <tbody>
          {payments.map((payment) => {
            return (
              <tr key={payment.id}>
                <th scope="row">{payment.id}</th>
                <td>{payment.description}</td>
                <td>{payment.createdOn}</td>
                <td>{payment.amount} €</td>
                <td>{payment.user.name}</td>
                <td>
                  {payment.paid ? (
                    <span className="text-success">Yes</span>
                  ) : (
                    <button
                      className="btn btn-primary"
                      onClick={() => {
                        handlePaymentPay(payment.stripeLink);
                      }}
                    >
                      <i className="fa-solid fa-dollar"></i>
                    </button>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
