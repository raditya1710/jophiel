package org.iatoki.judgels.jophiel.user.profile.info;

import org.iatoki.judgels.play.Page;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public final class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionDao institutionDao;

    @Inject
    public InstitutionServiceImpl(InstitutionDao institutionDao) {
        this.institutionDao = institutionDao;
    }

    @Override
    public List<Institution> getInstitutionsByTerm(String term) {
        List<InstitutionModel> institutionModels = institutionDao.findSortedByFilters("id", "asc", term, 0, -1);

        return institutionModels.stream().map(m -> InstitutionServiceUtils.createInstitutionFromModel(m)).collect(Collectors.toList());
    }

    @Override
    public boolean institutionExistsByName(String name) {
        return institutionDao.existsByName(name);
    }

    @Override
    public Institution findInstitutionById(long institutionId) throws InstitutionNotFoundException {
        InstitutionModel institutionModel = institutionDao.findById(institutionId);

        if (institutionModel == null) {
            throw new InstitutionNotFoundException("Institution Not Found.");
        }

        return InstitutionServiceUtils.createInstitutionFromModel(institutionModel);
    }

    @Override
    public void createInstitution(String name, String userJid, String userIpAddress) {
        InstitutionModel institutionModel = new InstitutionModel();
        institutionModel.institution = name;
        institutionModel.referenceCount = 0;

        institutionDao.persist(institutionModel, userJid, userIpAddress);
    }

    @Override
    public void deleteInstitution(long institutionId) throws InstitutionNotFoundException {
        InstitutionModel institutionModel = institutionDao.findById(institutionId);

        if (institutionModel == null) {
            throw new InstitutionNotFoundException("Institution Not Found.");
        }

        institutionDao.remove(institutionModel);
    }

    @Override
    public Page<Institution> getPageOfInstitutions(long pageIndex, long pageSize, String orderBy, String orderDir, String filterString) {
        long totalPages = institutionDao.countByFilters(filterString);
        List<InstitutionModel> institutionModels = institutionDao.findSortedByFilters(orderBy, orderDir, filterString, pageIndex * pageSize, pageSize);

        List<Institution> clients = institutionModels.stream().map(m -> InstitutionServiceUtils.createInstitutionFromModel(m)).collect(Collectors.toList());

        return new Page<>(clients, totalPages, pageIndex, pageSize);
    }
}
