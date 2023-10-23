package telran.java48.person.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java48.person.dao.PersonRepository;
import telran.java48.person.dto.AddressDto;
import telran.java48.person.dto.CityPopulationDto;
import telran.java48.person.dto.PersonDto;
import telran.java48.person.dto.exception.PersonNotFoundException;
import telran.java48.person.model.Address;
import telran.java48.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.delete(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
//		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(addressDto, Address.class));
//		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findByAddressCityIgnoreCase(city)
	            .map(person -> modelMapper.map(person, PersonDto.class))
	            .collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findByNameIgnoreCase(name)
	            .map(person -> modelMapper.map(person, PersonDto.class))
	            .collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
	        LocalDate currentDate = LocalDate.now();
	        LocalDate minAgeDate = currentDate.minus(maxAge, ChronoUnit.YEARS);
	        LocalDate maxAgeDate = currentDate.minus(minAge, ChronoUnit.YEARS);

		return personRepository.findByBirthDateBetween(minAgeDate, maxAgeDate)
	            .map(person -> modelMapper.map(person, PersonDto.class))
	            .collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		// TODO Auto-generated method stub
		return null;
	}

}
