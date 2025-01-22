describe('should be able to place bid and then disabled the submit', () => {
    it('passes', () => {
        cy.visit('https://localhost:5173/login');

        loginAsAdmin();

        cy.get("#dropdownMenuButton1").click();
        cy.get('.dropdown-item').contains('Create auction').click();

        fillAuctionDetails();
        cy.get("[type=submit]").click();

        cy.wait(1000);

        cy.get(".next-page-btn").then(($btn) => {
            if (!$btn.is(':disabled')) {
                cy.wrap($btn).click();
            }
        });

        cy.get(".next-page-btn").then(($btn) => {
            if (!$btn.is(':disabled')) {
                cy.wrap($btn).click();
            }
        });

        cy.get(".next-page-btn").then(($btn) => {
            if (!$btn.is(':disabled')) {
                cy.wrap($btn).click();
            }
        });

        cy.get('.btn-success').last().click();

        cy.wait(1000);

        cy.get(".place-bid-btn").should("not.be.disabled");

        placeBid();
    });

    function placeBid() {
        cy.get(".bid-input").type("2");
        cy.get(".place-bid-btn").click();

        cy.get(".place-bid-btn").should("have.class", "disabled");
    }

    function fillAuctionDetails() {
        cy.get("#description").type("Test auction by CyPress");
        cy.get("#minBidAmount").type("1");
        cy.get("#endDate").type("2026-01-01T00:00:00");
        cy.get("#car").select("3");

    }

    function loginAsAdmin() {
        cy.get('#email').type("admin@admin.com");
        cy.get('#password').type("12345678");
        cy.get('[type=submit]').click();
    }
});