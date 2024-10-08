package com.francodavyd.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record AuthCreateRoleRequest(@Size(max = 3) List<String> roleListName) {
}