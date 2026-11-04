import org.testng.annotations.Test;

public class API02_GetBookingIds {

    @Test
    public void getAllBookingIds() {

    }

    @Test
    public void filterByExistingFirstName() {

    }

    @Test
    public void filterByExistingLastName() {

    }

    @Test
    public void filterByExistingFirstAndLastName() {
        
    }

    @Test
    public void filterByCheckInDate() {
    }

    @Test
    public void filterByCheckOutDate() {
    }

    @Test
    public void filterByAllSupportedParameters() {
    }

    @Test
    public void unknownFirstNameReturnsEmptyArray() {
    }

    @Test
    public void checkInDateBoundaryIsInclusive() {
    }

    @Test
    public void invalidCheckInDateFormatDoesNotCauseServerError() {
    }

    @Test
    public void impossibleCheckInDateDoesNotCauseServerError() {
    }

    @Test
    public void emptyNameFiltersAreTreatedAsAbsent() {
    }

    @Test
    public void extremelyLongFirstNameIsHandledSafely() {
    }

    @Test
    public void encodedSpecialCharactersAreHandled() {
    }

    @Test
    public void duplicateFirstNameParametersDoNotCauseServerError() {
    }

    @Test
    public void unsupportedQueryParametersAreIgnored() {
    }

    @Test
    public void nullLikeNameValuesReturnNoMatches() {
    }

    @Test
    public void getWithJsonBodyReturnsBookingIds() {
    }

    @Test
    public void getWithRandomTextBodyIsHandledSafely() {
    }

    @Test
    public void misspelledFilterIsIgnored() {
    }

    @Test
    public void putOnBookingCollectionReturnsNotFound() {
    }

    @Test
    public void patchOnBookingCollectionReturnsNotFound() {
    }

    @Test
    public void postOnBookingCollectionReturnsObservedServerError() {
    }
}
