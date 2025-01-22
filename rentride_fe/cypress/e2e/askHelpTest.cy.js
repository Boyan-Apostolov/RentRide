
describe('help request correctly shows for admin', () => {
    it('passes', () => {
        cy.visit('https://localhost:5173/login')

        cy.get('.fa-circle-user').should("not.exist");

        cy.get('#email').type("admin@admin.com");
        cy.get('#password').type("12345678");
        cy.get('[type=submit]').click();

        cy.contains('button', 'Ask for help').click();

        const randomNumber = Math.floor(Math.random() * 1000) + 1;
        const expectedText = `Test help request ${randomNumber}`;
        cy.get('#swal2-input').type(expectedText);

        cy.get('.swal2-confirm').click();

        cy.get("#dropdownMenuButton1").click();
        cy.get('.dropdown-item').contains('All support messages').click();

        cy.get('#root').should("contain", expectedText);
    })
})
