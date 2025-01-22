
describe('profile page accessible after login', () => {
  it('passes', () => {
    cy.visit('https://localhost:5173/login')

    cy.get('.fa-circle-user').should("not.exist");

    cy.get('#email').type("customer@customer.com");
    cy.get('#password').type("12345678");
    cy.get('[type=submit]').click();

    cy.get('.fa-circle-user').should("be.visible");
  })
})


describe('profile page load data for correct session user', () => {
  it('passes', () => {
    cy.visit('https://localhost:5173/login')

    cy.get('#email').type("customer@customer.com");
    cy.get('#password').type("12345678");
    cy.get('[type=submit]').click();

    cy.get('.fa-circle-user').click();

    cy.get('.profile-page').should("contain", "Average Customer");
  })
})