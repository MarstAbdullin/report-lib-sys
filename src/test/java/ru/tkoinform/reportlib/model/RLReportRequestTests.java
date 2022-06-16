package ru.tkoinform.reportlib.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.util.TestUtils;

public class RLReportRequestTests {

    @Test
    public void columnCountAreEqualsBeforeAndAfterSorting() {
        RLReportRequest request = TestUtils.mockRequest(ReportFormat.XLS);

        int sizeBeforeSort = request.getColumns().size();

        request.sortColumns();

        Assertions.assertThat(request.getColumns().size())
                  .as("columns size").isEqualTo(sizeBeforeSort);
    }
}


