package de.gerritstapper.casscheduler.models.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturingFormsInformation {

    private String lecturingForms;
    private String lecturingMethods;
}
