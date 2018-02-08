package ats.database.models.listeners;

import javax.persistence.PostPersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ats.database.models.Company;
import ats.database.models.Listing;
import ats.database.repositories.CompaniesRepository;
import ats.utils.BeanUtil;

@Service
public class ListingsListener {
	
	private static Logger logger = LoggerFactory.getLogger(ListingsListener.class);
	
	@PostPersist
	public void listingsPostPersist(Listing listing) {
		try {
			Company company = listing.getLister().getCompany();
			company.setNumListingsRemaining(company.getNumListingsRemaining() - 1);
			CompaniesRepository companyRepository = BeanUtil.getBean(CompaniesRepository.class);
			companyRepository.save(company);
			logger.info("Updated num listings remaining for company: " + company.getCompanyName());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
