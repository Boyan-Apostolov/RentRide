describe('search should show correct cars and cannot book without login', () => {
  it('passes', () => {
    cy.visit('https://localhost:5173/search?fromCity=1&toCity=2&fromDate=2024-12-08&toDate=2024-12-10&fromTime=22%3A22&toTime=22%3A22')

    const carComponents = cy.get('.car-component', { timeout: 3000 })

    carComponents.should('be.visible');
    carComponents.should('have.length', 2);
    carComponents.should('contain', "Ford - Fiesta");
    carComponents.should('contain', "Login to book");
  })
});