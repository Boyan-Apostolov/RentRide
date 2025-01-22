describe('template spec', () => {
  it('passes', () => {
    cy.visit('https://localhost:5173/')

    cy.get('.hero-content').should('contain', "Easy, Fast, Yours")
  })
})