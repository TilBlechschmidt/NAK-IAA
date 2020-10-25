package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SurveyRepository extends Repository<Survey,Long> {

    List<Survey> findByCreator(User creator);

    Survey save(Survey toSave);

}
