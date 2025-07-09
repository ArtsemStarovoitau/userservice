package by.starovoytov.userservice.mapper;

import by.starovoytov.userservice.dto.CardInfoDto;
import by.starovoytov.userservice.model.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardInfoMapper {
    @Mapping(source = "user.id", target = "userId")
    CardInfoDto toDto(CardInfo entity);

    @Mapping(target = "user", ignore = true)
    CardInfo toEntity(CardInfoDto dto);
    
    @Mapping(target = "user", ignore = true)
    void updateCardInfoFromDto(CardInfoDto dto, @MappingTarget CardInfo entity);
}