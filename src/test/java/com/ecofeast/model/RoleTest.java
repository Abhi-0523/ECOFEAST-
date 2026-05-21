package com.ecofeast.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class RoleTest {

    @Test
    public void testConstructorsAndAccessors() {
        // Test default constructor
        Role role1 = new Role();
        role1.setRoleId(1);
        role1.setRoleName("ADMIN");
        role1.setDescription("Administrator role");

        assertEquals(1, role1.getRoleId());
        assertEquals("ADMIN", role1.getRoleName());
        assertEquals("Administrator role", role1.getDescription());

        // Test parameterized constructor
        Role role2 = new Role(2, "DONOR");
        role2.setDescription("Food donor role");
        
        assertEquals(2, role2.getRoleId());
        assertEquals("DONOR", role2.getRoleName());
        assertEquals("Food donor role", role2.getDescription());
    }
}
