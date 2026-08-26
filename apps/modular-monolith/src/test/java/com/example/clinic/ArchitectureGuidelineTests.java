package com.example.clinic;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureGuidelineTests {

    private static final String ROOT_PACKAGE = "com.example.clinic";

    @Test
    void appointmentsMustNotDependOnPatientInternals() {
        var classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(ROOT_PACKAGE);

        noClasses()
                .that().resideInAPackage("..appointments..")
                .should().dependOnClassesThat().resideInAPackage("..patients.internal..")
                .because("appointments must use the patients::api named interface only")
                .check(classes);
    }

    @Test
    void domainModulesMustNotDependOnSharedInternals() {
        var classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(ROOT_PACKAGE);

        noClasses()
                .that().resideInAnyPackage("..patients..", "..appointments..")
                .should().dependOnClassesThat().resideInAPackage("..shared.internal..")
                .because("domain modules must use the shared::api named interface only")
                .check(classes);
    }
}
