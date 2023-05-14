package com.aptech.coursemanagementserver.dtos;

import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TagDto extends BaseDto {
    private String name;
}
