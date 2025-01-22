describe('booking page should load correct data and prices', () => {
    it('passes', () => {
        login();

        populateSearchForm();

        cy.get("[data-bookbthcarid=1]").click();

        cy.get('.car-component').should('contain', 'Ford - Fiesta');


        cy.get('.from-city-text').should('contain', 'Amsterdam');
        cy.get('.to-city-text').should('contain', 'Breda');
    })

    function login() {
        cy.visit('https://localhost:5173/login')

        cy.get('#email').type("customer@customer.com");
        cy.get('#password').type("12345678");
        cy.get('[type=submit]').click();
    }

    function populateSearchForm() {
        cy.get('.pickup-city').select('Amsterdam')

        cy.get('.pickup-date').type("2025-05-01")

        cy.get('.pickup-time').type('10:00')

        cy.get('.dropoff-city').select('Breda')

        cy.get('.dropoff-date').type("2025-05-02")

        cy.get('.dropoff-time').type('15:00')

        cy.get('[type=submit]').click();

    }
});