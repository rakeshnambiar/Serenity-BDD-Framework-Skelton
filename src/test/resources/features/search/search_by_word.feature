@component:UI
@version:SIT-Release-1
@issue:EBI-001
Feature: Search by keyword
  In order to provide the details of Genes, Proteins or Chemical
  As a Scientist
  I should be able to enter the Search string on the Query field

  @TC01_EBI_SearchTest
  Scenario: Specific Search by Keyword
    Given I am Scientist
    When I open the EBI Website
    And Enter the keyword "Glycosyl transferases" on the Query field
    And Click on the Search button
    Then I should be able to see the matching results on the Search Result page