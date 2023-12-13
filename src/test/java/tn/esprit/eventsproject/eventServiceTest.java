package tn.esprit.eventsproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;
import tn.esprit.eventsproject.services.EventServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class eventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;
    @InjectMocks
    private EventServicesImpl eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Order(1)
    public void testAddParticipant() {

        Participant sampleParticipant = new Participant();
        sampleParticipant.setIdPart(1);

        when(participantRepository.save(any(Participant.class))).thenReturn(sampleParticipant);


        Participant savedParticipant = eventService.addParticipant(sampleParticipant);

        verify(participantRepository, times(1)).save(any(Participant.class));


        assertEquals(sampleParticipant, savedParticipant);
    }

   @Test
   @Order(2)
   public void testAddAffectEvenParticipant() {

       Participant sampleParticipant = new Participant();
       sampleParticipant.setIdPart(1);


       Event sampleEvent = new Event();
       sampleEvent.setIdEvent(1);


       when(participantRepository.findById(1)).thenReturn(Optional.of(sampleParticipant));


       when(eventRepository.save(any(Event.class))).thenReturn(sampleEvent);


       Event savedEvent = eventService.addAffectEvenParticipant(sampleEvent, 1);


       verify(eventRepository, times(1)).save(any(Event.class));


       assertTrue(sampleParticipant.getEvents().contains(sampleEvent));


       assertEquals(sampleEvent, savedEvent);
   }

    @Test
    @Order(3)
    public void testAddAffectEvenParticipants() {

        Participant participant1 = new Participant();
        participant1.setIdPart(1);

        Participant participant2 = new Participant();
        participant2.setIdPart(2);


        Event sampleEvent = new Event();
        sampleEvent.setIdEvent(1);
        Set<Participant> participants = new HashSet<>();
        participants.add(participant1);
        participants.add(participant2);
        sampleEvent.setParticipants(participants);


        when(participantRepository.findById(1)).thenReturn(Optional.of(participant1));
        when(participantRepository.findById(2)).thenReturn(Optional.of(participant2));


        when(eventRepository.save(any(Event.class))).thenReturn(sampleEvent);


        Event savedEvent = eventService.addAffectEvenParticipant(sampleEvent);


        verify(eventRepository, times(1)).save(any(Event.class));


        assertTrue(participant1.getEvents().contains(sampleEvent));
        assertTrue(participant2.getEvents().contains(sampleEvent));


        assertEquals(sampleEvent, savedEvent);
    }

    @Test
    @Order(4)
    public void testAddAffectLog() {

        Event sampleEvent = new Event();
        sampleEvent.setDescription("Sample Event");


        Logistics sampleLogistics = new Logistics();
        sampleLogistics.setIdLog(1);


        when(eventRepository.findByDescription("Sample Event")).thenReturn(sampleEvent);


        when(logisticsRepository.save(any(Logistics.class))).thenReturn(sampleLogistics);


        Logistics savedLogistics = eventService.addAffectLog(sampleLogistics, "Sample Event");


        verify(logisticsRepository, times(1)).save(any(Logistics.class));

        assertTrue(sampleEvent.getLogistics().contains(sampleLogistics));


        assertEquals(sampleLogistics, savedLogistics);
    }

    @Test
    @Order(5)
    public void testGetLogisticsDates() {

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);


        Event sampleEvent = new Event();
        sampleEvent.setDateDebut(LocalDate.of(2023, 5, 1));

        Logistics logistics1 = new Logistics();
        logistics1.setReserve(true);

        Logistics logistics2 = new Logistics();
        logistics2.setReserve(false);

        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(logistics1);
        logisticsSet.add(logistics2);

        sampleEvent.setLogistics(logisticsSet);


        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(Arrays.asList(sampleEvent));


        List<Logistics> logisticsList = eventService.getLogisticsDates(startDate, endDate);


        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);


        assertEquals(1, logisticsList.size());
        assertTrue(logisticsList.contains(logistics1));
        assertFalse(logisticsList.contains(logistics2));
    }


}
