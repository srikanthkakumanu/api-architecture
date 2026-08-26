const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export type Patient = {
  id: number;
  fullName: string;
  email: string;
  phoneNumber: string;
  dateOfBirth: string;
};

export type Appointment = {
  id: number;
  patientId: number;
  patientName: string;
  doctorName: string;
  startsAt: string;
  reason: string;
  status: "SCHEDULED";
};

export type RegisterPatientRequest = Omit<Patient, "id">;

export type ScheduleAppointmentRequest = {
  patientId: number;
  doctorName: string;
  startsAt: string;
  reason: string;
};

export async function getPatients(): Promise<Patient[]> {
  return request<Patient[]>("/patients", { cache: "no-store" });
}

export async function createPatient(payload: RegisterPatientRequest): Promise<Patient> {
  return request<Patient>("/patients", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function getAppointments(): Promise<Appointment[]> {
  return request<Appointment[]>("/appointments", { cache: "no-store" });
}

export async function createAppointment(payload: ScheduleAppointmentRequest): Promise<Appointment> {
  return request<Appointment>("/appointments", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...init.headers,
    },
  });

  if (!response.ok) {
    const problem = (await response.json().catch(() => null)) as { detail?: string } | null;
    throw new Error(problem?.detail ?? `Request failed with ${response.status}`);
  }

  return (await response.json()) as T;
}
